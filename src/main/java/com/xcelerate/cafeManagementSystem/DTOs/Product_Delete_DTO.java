package com.xcelerate.cafeManagementSystem.DTOs;

import jakarta.validation.constraints.NotNull;

public class Product_Delete_DTO {
    @NotNull
    private long productId;

    @NotNull
    public long getProductId() {
        return productId;
    }

    public void setProductId(@NotNull long productId) {
        this.productId = productId;
    }
}
