package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.Service.CustomerService;
import com.xcelerate.cafeManagementSystem.Service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String phone
    ) {
        // Generate and send OTP
        customerService.createUser(email, password, name, phone);
        otpService.generateAndSendOtp(email);

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