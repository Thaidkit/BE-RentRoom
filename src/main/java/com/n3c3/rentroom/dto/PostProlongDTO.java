package com.n3c3.rentroom.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PostProlongDTO {
    private Integer amountDaysProlong;
    private Long amountMoneyPayment;

    public Integer getAmountDaysProlong() {
        return amountDaysProlong;
    }

    public void setAmountDaysProlong(Integer amountDaysProlong) {
        this.amountDaysProlong = amountDaysProlong;
    }

    public Long getAmountMoneyPayment() {
        return amountMoneyPayment;
    }

    public void setAmountMoneyPayment(Long amountMoneyPayment) {
        this.amountMoneyPayment = amountMoneyPayment;
    }
}
