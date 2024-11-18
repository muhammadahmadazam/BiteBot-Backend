package com.xcelerate.cafeManagementSystem.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "Customer")
public class Customer extends User {

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private String name;
    private String Phone;
    private boolean verified;

    public Customer() {}

    public Customer(String name, String phone, User user) {
        this.user = user;
        this.name = name;
        this.Phone = phone;
        this.verified = false;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean getVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
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
