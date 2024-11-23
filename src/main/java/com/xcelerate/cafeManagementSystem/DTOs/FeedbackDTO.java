package com.xcelerate.cafeManagementSystem.DTOs;

import com.xcelerate.cafeManagementSystem.Model.Order;

public class FeedbackDTO {
    public String feedback_id;
    public String content;
    public String emotion;
    public String customer_name;
    public long customer_id;
    public PastOrderDTO order;

    public FeedbackDTO(String feedback_id, String content, String emotion, PastOrderDTO order, String customer_name, long customer_id) {
        this.feedback_id = feedback_id;
        this.content = content;
        this.emotion = emotion;
        this.order = order;
        this.customer_name = customer_name;
        this.customer_id = customer_id;
    }
}
