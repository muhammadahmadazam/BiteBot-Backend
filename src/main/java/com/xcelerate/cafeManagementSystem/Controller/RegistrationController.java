package com.xcelerate.cafeManagementSystem.Controller;

import com.cloudinary.Api;
import com.xcelerate.cafeManagementSystem.DTOs.ApiResponseDTO;
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
    public ResponseEntity<ApiResponseDTO<String>> registerUser(
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
            ApiResponseDTO<String> response = new ApiResponseDTO<>("OTP sent to email: " + email, email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponseDTO<String> response = new ApiResponseDTO<>("Error in sending OTP", email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDTO<String>> verifyOtp(@RequestBody CustomerVerificationDTO customerVerificationDTO) {
        String email = customerVerificationDTO.email;
        String otp = customerVerificationDTO.otp;
        if(EmailUtil.verifyEmailFormat(email) == false) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Invalid Email", email);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        }
        if (otpService.validateOtp(email, otp)) {
            customerService.verifyUser(email);
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Email verified successfully", email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponseDTO<String> response = new ApiResponseDTO<>("Invalid OTP", email);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<ApiResponseDTO<String>> resendOtp(@RequestBody EmailDTO emailDTO) {

        String email = emailDTO.email;

        if(EmailUtil.verifyEmailFormat(email) == false) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Invalid Email", email);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        int isVerifiedStatus = customerService.isCustomerVerified(email);
        // 0 if the customer does not exist, 1 if the customer is not verified, 2 if the customer is verified

        if(isVerifiedStatus == 0) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Email does not exist", email);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        else if(isVerifiedStatus == 2) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Email is already verified", email);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        boolean isOtpSent = otpService.generateAndSendOtp(email);
        if(isOtpSent) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("OTP resent to email: " + email, email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponseDTO<String> response = new ApiResponseDTO<>("Error in sending OTP", email);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}