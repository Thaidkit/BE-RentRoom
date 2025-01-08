package com.n3c3.rentroom.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class OrderPaymentUpdateDTO {
    private long orderId;
    private String status;
    private Boolean isCancelled;

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean cancelled) {
        isCancelled = cancelled;
    }
}
