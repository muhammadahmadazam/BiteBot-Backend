package com.xcelerate.cafeManagementSystem.DTOs;

import jakarta.validation.constraints.NotNull;

public class Order_Failed_DTO {
    @NotNull
    private String orderId;
    @NotNull
    private  String reason;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public @NotNull String getReason() {
        return reason;
    }

    public void setReason(@NotNull String reason) {
        this.reason = reason;
    }
}
