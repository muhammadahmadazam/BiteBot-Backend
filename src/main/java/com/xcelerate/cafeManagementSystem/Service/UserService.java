package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.Model.User;
import com.xcelerate.cafeManagementSystem.Model.Worker;
import com.xcelerate.cafeManagementSystem.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService
{
    @Autowired
    UserRepository userRepository;

    UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public void  createUser(User user){
        userRepository.save(user);
    }
}
