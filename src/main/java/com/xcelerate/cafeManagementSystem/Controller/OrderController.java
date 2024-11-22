package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.Model.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/")
public class OrderController {

    @RequestMapping("/order/create")
    public void createOrder(Order o) {

    }
}
