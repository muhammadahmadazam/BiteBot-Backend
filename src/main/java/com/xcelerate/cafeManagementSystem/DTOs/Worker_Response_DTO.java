package com.xcelerate.cafeManagementSystem.DTOs;

import com.xcelerate.cafeManagementSystem.Model.DeliveryMan;
import com.xcelerate.cafeManagementSystem.Model.Worker;

import java.util.Date;

public class Worker_Response_DTO {
    public long workerId;
    public String role;
    public String email;
    public Long salary;
    public String position;
    public Date joinDate;
    public String vehicle;

    public Worker_Response_DTO (Worker w) {
        this.workerId = w.getId();
        this.email = w.getEmail();
        this.role = w.getRole();
        this.salary = w.getSalary();
        this.position = w.getPosition();
        this.joinDate = w.getJoinDate();
        if (w instanceof DeliveryMan) {
            this.vehicle = ((DeliveryMan) w).getVehicle();
        }
    }
}
