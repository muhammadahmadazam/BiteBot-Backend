package com.xcelerate.cafeManagementSystem.DTOs;

public class ApiResponseDTO<T> {
    public String message;
    public T data;

    public ApiResponseDTO(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ApiResponseDTO() {

    }
}
