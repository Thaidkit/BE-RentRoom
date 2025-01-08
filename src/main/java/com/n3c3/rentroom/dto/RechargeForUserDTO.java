package com.n3c3.rentroom.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class RechargeForUserDTO {
    private Long userId;
    private String productName = "Nạp tiền tài khoản";
    private Integer amountPayment;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getAmountPayment() {
        return amountPayment;
    }

    public void setAmountPrice(Integer price) {
        this.amountPayment = price;
    }

}
