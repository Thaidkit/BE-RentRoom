package com.n3c3.rentroom.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.n3c3.rentroom.dto.OrderPaymentUpdateDTO;
import com.n3c3.rentroom.dto.RechargeForUserDTO;
import com.n3c3.rentroom.service.OrderPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class OrderPaymentController {

    private final OrderPaymentService orderPaymentService;

    public OrderPaymentController(OrderPaymentService orderPaymentService) {
        this.orderPaymentService = orderPaymentService;
    }

    @PostMapping("/create-payment-link")
    public ObjectNode createPaymentLink(@RequestBody RechargeForUserDTO rechargeForUserDTO) {
        return orderPaymentService.createPaymentLink(rechargeForUserDTO);
    }

    @GetMapping(path = "/{orderId}")
    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
        return orderPaymentService.getOrderById(orderId);
    }

    @PostMapping(path = "/confirm-webhook")
    public ObjectNode confirmWebhook(@RequestBody  String requestBody) {
        return orderPaymentService.confirmWebhook(requestBody);
    }

    @PostMapping("/update-order-payment")
    public ResponseEntity<?> updateOrderPayment(@RequestBody OrderPaymentUpdateDTO orderPaymentUpdateDTO) {
        return orderPaymentService.updateOrderPayment(orderPaymentUpdateDTO);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<?> getOrderPaymentHistory(@PathVariable("userId") Long userId) {
        return orderPaymentService.getHistoryPaymentUser(userId);
    }
}
