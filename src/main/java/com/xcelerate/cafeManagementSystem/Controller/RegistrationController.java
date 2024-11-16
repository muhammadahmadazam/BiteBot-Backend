package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.CustomerRegistrationDTO;
import com.xcelerate.cafeManagementSystem.Service.CustomerService;
import com.xcelerate.cafeManagementSystem.Service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/register")
//@CrossOrigin("http://localhost:3000")
public class RegistrationController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OtpService otpService;

    @PostMapping("/create-account")
    public ResponseEntity<String> registerUser(
            @RequestBody CustomerRegistrationDTO customerRegistrationDTO
    ) {
        String email = customerRegistrationDTO.email;
        String password = customerRegistrationDTO.password;
        String name = customerRegistrationDTO.name;
        String phone = customerRegistrationDTO.phone;

        // Generate User and send OTP
      //  customerService.createUser(email, password, name, phone);
        boolean isOtpSent = otpService.generateAndSendOtp(email);


        if(isOtpSent) {
            return new ResponseEntity<>("User registered successfully, Error in sending OTP", HttpStatus.OK);
        }



        return new ResponseEntity<>("OTP sent to email: " + email, HttpStatus.OK);
    }

    @PostMapping("/verify-otp")

    public ResponseEntity<String> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp
    ) {
        if (otpService.validateOtp(email, otp)) {
            return new ResponseEntity<>("OTP verified successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
    }
}