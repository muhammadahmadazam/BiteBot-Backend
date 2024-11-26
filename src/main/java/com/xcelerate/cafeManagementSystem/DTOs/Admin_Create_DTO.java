package com.xcelerate.cafeManagementSystem.DTOs;

import jakarta.validation.constraints.NotNull;

public class Admin_Create_DTO {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private String cafeSecretKey;
    @NotNull
    private String phoneNumber;

    public @NotNull String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(@NotNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public @NotNull String getEmail() {
        return email;
    }

    public void setEmail(@NotNull String email) {
        this.email = email;
    }

    public @NotNull String getPassword() {
        return password;
    }

    public void setPassword(@NotNull String password) {
        this.password = password;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getCafeSecretKey() {
        return cafeSecretKey;
    }

    public void setCafeSecretKey(@NotNull String cafeSecretKey) {
        this.cafeSecretKey = cafeSecretKey;
    }
}
