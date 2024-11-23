package com.xcelerate.cafeManagementSystem.DTOs;

import java.util.Date;
import java.util.List;

public class PastOrderDTO {
    public String order_id;
    public float total_price;
    public String order_status;
    public Date order_date;
    public String payment_method;
    public String delivery_address;
    public List<SalesLineItemDTO> order_items;

    public PastOrderDTO(String order_id,float total_price, String order_status, Date order_date, String payment_method, String delivery_address, List<SalesLineItemDTO> order_items) {
        this.order_id = order_id;
        this.total_price = total_price;
        this.order_status = order_status;
        this.order_date = order_date;
        this.payment_method = payment_method;
        this.delivery_address = delivery_address;
        this.order_items = order_items;

    }
}
