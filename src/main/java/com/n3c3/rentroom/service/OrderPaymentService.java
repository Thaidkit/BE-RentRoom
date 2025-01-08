package com.n3c3.rentroom.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.n3c3.rentroom.dto.ObjectResponse;
import com.n3c3.rentroom.dto.OrderPaymentUpdateDTO;
import com.n3c3.rentroom.dto.RechargeForUserDTO;
import com.n3c3.rentroom.entity.OrderPayment;
import com.n3c3.rentroom.entity.StatusPayment;
import com.n3c3.rentroom.entity.User;
import com.n3c3.rentroom.repository.OrderPaymentRepository;
import com.n3c3.rentroom.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;
import vn.payos.type.PaymentLinkData;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Map;


@Service
public class OrderPaymentService {

    private final PayOS payOS;
    private final UserRepository userRepository;
    private final OrderPaymentRepository orderPaymentRepository;

    @Value("${rent-room.domain}")
    private String DOMAIN;

    public OrderPaymentService(PayOS payOS,
                               UserRepository userRepository,
                               OrderPaymentRepository orderPaymentRepository) {
        super();
        this.payOS = payOS;
        this.userRepository = userRepository;
        this.orderPaymentRepository = orderPaymentRepository;
    }

    public ObjectNode createPaymentLink(RechargeForUserDTO rechargeForUserDTO){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            User user = userRepository.findById(rechargeForUserDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found") );

            // tạo đối soát
            OrderPayment orderPayment = new OrderPayment();
            orderPayment.setAmountPayment(rechargeForUserDTO.getAmountPayment());
            orderPayment.setStatusPayment(StatusPayment.PENDING);
            orderPayment.setUser(user);

            StringBuilder desc = new StringBuilder("Người dùng id ").append(user.getId()).append(" nạp tiền!");

            orderPayment.setDescription(desc.toString());
            orderPaymentRepository.save(orderPayment);

            // tạo link thanh toán
            final String productName = rechargeForUserDTO.getProductName();

            final String returnUrl = DOMAIN + "/payment-success";
            final String cancelUrl = DOMAIN + "/payment-cancel";

            final Integer price = rechargeForUserDTO.getAmountPayment();
            long orderCode = orderPayment.getId();

            ItemData item = ItemData.builder()
                    .name(productName)
                    .price(price)
                    .quantity(1)
                    .build();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .description(desc.toString())
                    .amount(price)
                    .item(item)
                    .returnUrl(returnUrl)
                    .cancelUrl(cancelUrl)
                    .build();

            CheckoutResponseData data = payOS.createPaymentLink(paymentData);

            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;
        }
    }

    public ObjectNode getOrderById(long orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            PaymentLinkData order = payOS.getPaymentLinkInformation(orderId);

            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    public ObjectNode confirmWebhook(String requestBody){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String str = payOS.confirmWebhook(requestBody);
            response.set("data", objectMapper.valueToTree(str));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    public ResponseEntity<?> updateOrderPayment(OrderPaymentUpdateDTO orderPaymentUpdateDTO){
        try {

            OrderPayment orderPayment = orderPaymentRepository.findById(orderPaymentUpdateDTO.getOrderId())
                    .orElseThrow(() -> new RuntimeException("Order payment not found"));

            if (orderPaymentUpdateDTO.getIsCancelled().equals(Boolean.TRUE)){
                return ResponseEntity.status(200).body(new ObjectResponse(200, "cancelled", "Payment canceled"));
            }
            else {
                switch (orderPaymentUpdateDTO.getStatus().trim().toLowerCase(Locale.ROOT))
                {
                    case "paid":
                        User user = userRepository.findById(orderPayment.getUser().getId()).orElseThrow(() -> new RuntimeException("User not found"));
                        user.setTotalMoney(user.getTotalMoney()+orderPayment.getAmountPayment());
                        userRepository.save(user);

                        orderPayment.setStatusPayment(StatusPayment.SUCCESS);
                        orderPayment.setModifyAt(LocalDate.now());
                        orderPaymentRepository.save(orderPayment);
                        break;
                    case "pending", "processing": break;
                    case "cancelled" :
                        orderPayment.setStatusPayment(StatusPayment.CANCELLED);
                        orderPayment.setModifyAt(LocalDate.now());
                        orderPaymentRepository.save(orderPayment);
                        break;
                }
            }

            return ResponseEntity.status(200).body(new ObjectResponse(200, "success", orderPayment));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body(new ObjectResponse(500, "Internal Error Server", e.getMessage()));
        }
    }
}
