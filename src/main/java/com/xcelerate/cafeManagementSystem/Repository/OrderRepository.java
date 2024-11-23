package com.xcelerate.cafeManagementSystem.Repository;

import com.xcelerate.cafeManagementSystem.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByCustomerIdOrStatusNot(long customer, String status);
    List<Order> findAllByCustomerId(long customer);
    Order findByOrderId(String orderId);
}
