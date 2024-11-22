package com.xcelerate.cafeManagementSystem.Model;


import jakarta.persistence.*;

@Entity
@Table(name = "salesLineItems")
public class SalesLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long quantity;
    private Long unitPrice;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;


    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }


}
