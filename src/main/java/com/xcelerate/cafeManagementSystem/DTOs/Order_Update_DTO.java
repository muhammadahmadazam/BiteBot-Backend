package com.xcelerate.cafeManagementSystem.DTOs;


import jakarta.validation.constraints.NotNull;

public class Order_Update_DTO {
    @NotNull
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
