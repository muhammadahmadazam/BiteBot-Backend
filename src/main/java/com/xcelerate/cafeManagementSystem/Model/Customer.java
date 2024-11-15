package com.xcelerate.cafeManagementSystem.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Customers")
public class Customer extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String Phone;
    private boolean Verified;

    public Customer(String email, String password, String name, String phone) {
        super(email, password);
        this.name = name;
        this.Phone = phone;
        this.Verified = false;
    }

    public boolean getVerified() {
        return Verified;
    }

    public void setVerified(boolean verified) {
        this.Verified = verified;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }
}
