package com.xcelerate.cafeManagementSystem.Repository;

import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
