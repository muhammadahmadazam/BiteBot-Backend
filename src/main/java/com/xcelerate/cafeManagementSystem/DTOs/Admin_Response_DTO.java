package com.xcelerate.cafeManagementSystem.DTOs;

import com.xcelerate.cafeManagementSystem.Model.Admin;
import jakarta.validation.constraints.NotNull;

public class Admin_Response_DTO {
    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String phoneNumber;

    @NotNull
    private long id;

    @NotNull
    private String role;

    public Admin_Response_DTO(Admin admin) {
        this.email = admin.getEmail();
        this.name = admin.getName();
        this.phoneNumber = admin.getPhoneNumber();
        this.id = admin.getId();
        this.role = admin.getRole();
    }

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NotNull
    public long getId() {
        return id;
    }

    public void setId(@NotNull long id) {
        this.id = id;
    }

    public @NotNull String getRole() {
        return role;
    }

    public void setRole(@NotNull String role) {
        this.role = role;
    }
}
