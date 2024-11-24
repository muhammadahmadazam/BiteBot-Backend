package com.xcelerate.cafeManagementSystem.DTOs;
import java.util.List;

public class OrderRequest {
    private String paymentMethod;
    private String  address;
    private String latitude;
    private String longitude;
    private String sector;
    private List<LineItemRequest> lineItems;


    // Getters and Setters
    public static class LineItemRequest {
        private long productId;
        private int quantity;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public List<LineItemRequest> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItemRequest> lineItems) {
        this.lineItems = lineItems;
    }
}