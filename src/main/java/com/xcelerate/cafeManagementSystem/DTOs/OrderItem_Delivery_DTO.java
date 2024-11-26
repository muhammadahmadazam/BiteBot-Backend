package com.xcelerate.cafeManagementSystem.DTOs;

import com.xcelerate.cafeManagementSystem.Model.Ingredient;
import com.xcelerate.cafeManagementSystem.Model.SalesLineItem;

import java.util.ArrayList;
import java.util.List;

public class OrderItem_Delivery_DTO {
    public long id;
    public String name;
    public  long price;
    public String category;
    public boolean availability;
    public List<Ingredient> ingredients ;
    public String imageLink;
    public String description;
    public long quantity;

    OrderItem_Delivery_DTO(SalesLineItem salesLineItem) {
        this.id = salesLineItem.getId();
        this.name =salesLineItem.getProduct().getName();
        this.price = (long)salesLineItem.getProduct().getPrice();
        this.category = salesLineItem.getProduct().getCategory();
        this.availability = salesLineItem.getProduct().isAvailability();
        this.ingredients = salesLineItem.getProduct().getIngredients().stream().toList();
        this.imageLink = salesLineItem.getProduct().getImageLink();
        this.description = salesLineItem.getProduct().getDescription();
        this.quantity = salesLineItem.getQuantity();
    }
}