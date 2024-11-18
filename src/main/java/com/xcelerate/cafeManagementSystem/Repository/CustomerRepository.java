package com.xcelerate.cafeManagementSystem.Repository;

import com.xcelerate.cafeManagementSystem.Model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("SELECT c FROM Customer c JOIN User u on c.id = u.id WHERE u.email = ?1")
    Customer findCustomerByEmail(String email);
}
