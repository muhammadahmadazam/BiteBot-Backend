package com.xcelerate.cafeManagementSystem.Controller;


import java.util.List;

import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/customer")
@CrossOrigin("http://localhost:3000")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<Customer> createUser(@RequestBody Customer c) {
        Customer createdCustomer = customerService.saveUser(c);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

}
