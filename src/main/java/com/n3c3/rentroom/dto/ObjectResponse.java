package com.n3c3.rentroom.dto;

import lombok.*;

@ToString
public class ObjectResponse {
    private Integer status;
    private String message;
    private Object data;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ObjectResponse(Integer status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
