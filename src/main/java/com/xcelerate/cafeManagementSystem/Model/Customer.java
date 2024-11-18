package com.xcelerate.cafeManagementSystem.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer extends User {

    private String name;
    private String Phone;
    private boolean verified;

    public Customer() {}

    public Customer(String name, String phone, String email, String password) {
        super(email, password);
        this.name = name;
        this.Phone = phone;
        this.verified = false;
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
