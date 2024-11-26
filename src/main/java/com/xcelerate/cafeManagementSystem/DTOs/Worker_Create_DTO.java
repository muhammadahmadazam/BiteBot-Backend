package com.xcelerate.cafeManagementSystem.DTOs;

import jakarta.validation.constraints.NotNull;

public class Worker_Create_DTO {
    @NotNull
    public String email;
    @NotNull
    public String password;
    @NotNull
    public Long salary;
    @NotNull
    public String position;
    @NotNull
    public String type;
    @NotNull
    public String vehicle;

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

    public @NotNull Long getSalary() {
        return salary;
    }

    public void setSalary(@NotNull Long salary) {
        this.salary = salary;
    }

    public @NotNull String getPosition() {
        return position;
    }

    public void setPosition(@NotNull String position) {
        this.position = position;
    }
}
