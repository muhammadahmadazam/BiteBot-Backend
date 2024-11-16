package com.xcelerate.cafeManagementSystem.Model;


import jakarta.persistence.*;

@MappedSuperclass
public class User {
    @Column(unique = true)
    protected String email;
    protected String password;

    protected User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }




}
