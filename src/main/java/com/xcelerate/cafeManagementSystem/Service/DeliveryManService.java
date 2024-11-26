package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.Repository.DeliveryManRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryManService {

    @Autowired
    DeliveryManRepository deliveryManRepository;
    public DeliveryManService(DeliveryManRepository deliveryManRepository) {
        this.deliveryManRepository = deliveryManRepository;
    }



}
