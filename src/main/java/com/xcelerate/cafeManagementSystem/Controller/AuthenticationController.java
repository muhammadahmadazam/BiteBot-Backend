package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.ApiResponseDTO;
import com.xcelerate.cafeManagementSystem.DTOs.LoginDTO;
import com.xcelerate.cafeManagementSystem.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<String>> loginUser(
            @RequestBody LoginDTO loginDTO
    ) {

        String email = loginDTO.email;
        String password = loginDTO.password;

        String token = authenticationService.loginUser(email, password);
        if(token != null) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Login successful", token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponseDTO<String> response = new ApiResponseDTO<>("Invalid credentials", null);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
