package com.xcelerate.cafeManagementSystem.DTOs;

public class StripeResponse {
    private String paymentURL;

    public String getPaymentURL() {
        return paymentURL;
    }

    public void setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
    }
}
