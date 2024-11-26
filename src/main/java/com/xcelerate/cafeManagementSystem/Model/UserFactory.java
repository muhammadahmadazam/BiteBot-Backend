package com.xcelerate.cafeManagementSystem.Model;

public class UserFactory {
    User user;
    public User createUser(String userType) {
        User user = switch (userType) {
            case "WORKER" -> new Worker();
            case "DELIVERY_MAN" -> new DeliveryMan();
            case "ADMIN" -> new Admin();
            default -> null;
        };
        return user;
    }
}
