package com.xcelerate.cafeManagementSystem.DTOs;

public class SalesLineItemDTO {

    public long quantity;
    public float unitPrice;
    public String productName;
    public String imageURL;

    public SalesLineItemDTO(long quantity, String productName, String imageURL, float unitPrice) {
        this.quantity = quantity;
        this.productName = productName;
        this.imageURL = imageURL;
        this.unitPrice = unitPrice;
    }
}
