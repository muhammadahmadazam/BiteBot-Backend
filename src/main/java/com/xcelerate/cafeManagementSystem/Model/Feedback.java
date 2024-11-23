package com.xcelerate.cafeManagementSystem.Model;


import jakarta.persistence.*;

@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(unique = true)
    public String feedback_id;

    public String content;

    public String emotion;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "orderId")
    public Order order;
}
