package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.CustomerRegistrationDTO;
import com.xcelerate.cafeManagementSystem.DTOs.CustomerVerificationDTO;
import com.xcelerate.cafeManagementSystem.DTOs.EmailDTO;
import com.xcelerate.cafeManagementSystem.Service.CustomerService;
import com.xcelerate.cafeManagementSystem.Service.OtpService;
import com.xcelerate.cafeManagementSystem.Utils.EmailUtil;
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


        // Generate Non Verified User and send OTP
        customerService.createUser(email, password, name, phone);
        boolean isOtpSent = otpService.generateAndSendOtp(email);


        if(isOtpSent) {
            return new ResponseEntity<>("OTP sent to email: " + email, HttpStatus.OK);
        }
        return new ResponseEntity<>("User registered successfully, Error in sending OTP", HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody CustomerVerificationDTO customerVerificationDTO) {
        String email = customerVerificationDTO.email;
        String otp = customerVerificationDTO.otp;
        if(EmailUtil.verifyEmailFormat(email) == false) {
            return new ResponseEntity<>("Invalid Email", HttpStatus.BAD_REQUEST);

        }
        if (otpService.validateOtp(email, otp)) {
            customerService.verifyUser(email);
            return new ResponseEntity<>("OTP verified successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid OTP", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody EmailDTO emailDTO) {

        String email = emailDTO.email;

        if(EmailUtil.verifyEmailFormat(email) == false) {
            return new ResponseEntity<>("Invalid Email", HttpStatus.BAD_REQUEST);
        }

        int isVerifiedStatus = customerService.isCustomerVerified(email);
        // 0 if the customer does not exist, 1 if the customer is not verified, 2 if the customer is verified

        if(isVerifiedStatus == 0) {
            return new ResponseEntity<>("Email does not Exist", HttpStatus.BAD_REQUEST);
        }

        else if(isVerifiedStatus == 2) {
            return new ResponseEntity<>("Email is already verified", HttpStatus.BAD_REQUEST);
        }

        boolean isOtpSent = otpService.generateAndSendOtp(email);
        if(isOtpSent) {
            return new ResponseEntity<>("OTP resent to email: " + email, HttpStatus.OK);
        }
        return new ResponseEntity<>("Error in sending OTP", HttpStatus.OK);
    }
}