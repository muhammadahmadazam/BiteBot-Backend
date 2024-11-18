package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.LoginDTO;
import com.xcelerate.cafeManagementSystem.Service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private CustomerService customerService;

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(
//            @RequestBody LoginDTO loginDTO
//    ) {
//        String email = loginDTO.email;
//        String password = loginDTO.password;
//
//        if (customerService.loginUser(email, password)) {
//            return new ResponseEntity<>("User logged in successfully", HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
//    }
}
