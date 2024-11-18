package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.User;
import com.xcelerate.cafeManagementSystem.Repository.CustomerRepository;
import com.xcelerate.cafeManagementSystem.Repository.UserRepository;
import com.xcelerate.cafeManagementSystem.Utils.JwtUtil;
import com.xcelerate.cafeManagementSystem.Utils.PasswordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthenticationService {


    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Autowired
    public  AuthenticationService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if(user == null || !PasswordUtil.checkPassword(password, user.getPassword())) {
            return null;
        }
        return jwtUtil.generateToken(email, user.getRole(), Long.toString(user.getId()));
    }
}
