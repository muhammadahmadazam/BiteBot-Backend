package com.xcelerate.cafeManagementSystem.DTOs;

import com.xcelerate.cafeManagementSystem.Model.Ingredient;
import com.xcelerate.cafeManagementSystem.Model.Order;
import com.xcelerate.cafeManagementSystem.Model.SalesLineItem;

import java.util.ArrayList;
import java.util.List;

public class Order_DeliveryDTO {

    public String orderId;
    public String address;
    public String paymentMethod;
    public String status;
    public Long price;

    public List<OrderItem_Delivery_DTO> lineItems;

    public Order_DeliveryDTO(Order o) {
        this.orderId=o.getOrderId();
        this.address=o.getAddress();
        this.paymentMethod=o.getPaymentMethod();
        this.status=o.getStatus();
        this.price = (long)o.getTotalPrice();
        List<SalesLineItem> saleItems = o.getOrderItems();
        this.lineItems = saleItems.stream().map(OrderItem_Delivery_DTO::new).toList();
    }



}
