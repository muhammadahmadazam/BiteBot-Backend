package com.xcelerate.cafeManagementSystem.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Table(name = "deliveryMen")
@OnDelete(action = OnDeleteAction.CASCADE)
public class DeliveryMan extends Worker {
    private String vehicle;

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}