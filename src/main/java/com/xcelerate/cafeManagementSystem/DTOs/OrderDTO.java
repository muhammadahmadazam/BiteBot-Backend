package com.xcelerate.cafeManagementSystem.DTOs;

import java.util.Date;
import java.util.List;

public class OrderDTO {
    public String order_id;
    public long customer_id;
    public float total_price;
    public String order_status;
    public Date order_date;
    public String estimated_time;
    public String payment_method;
    public String delivery_address;
    public List<SalesLineItemDTO> order_items;

    public OrderDTO(String order_id, float total_price, long customer_id, String order_status, Date order_date, String estimated_time, String payment_method, String delivery_address, List<SalesLineItemDTO> order_items) {
        this.order_id = order_id;
        this.total_price = total_price;
        this.customer_id = customer_id;
        this.order_status = order_status;
        this.order_date = order_date;
        this.estimated_time = estimated_time;
        this.payment_method = payment_method;
        this.delivery_address = delivery_address;
        this.order_items = order_items;
    }


}
