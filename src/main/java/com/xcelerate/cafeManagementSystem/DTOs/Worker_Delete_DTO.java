package com.xcelerate.cafeManagementSystem.DTOs;

import jakarta.validation.constraints.NotNull;

public class Worker_Delete_DTO {
    @NotNull
    private long workerId;

    @NotNull
    public long getWorkerId() {
        return workerId;
    }

    public void setWorkerId(@NotNull long workerId) {
        this.workerId = workerId;
    }
}
