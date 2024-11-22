package com.xcelerate.cafeManagementSystem.Repository;

import com.xcelerate.cafeManagementSystem.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByEmail(String email);

    Optional<Customer> findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);
}
