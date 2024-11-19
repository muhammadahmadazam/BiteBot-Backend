package com.xcelerate.cafeManagementSystem.Repository;
import com.xcelerate.cafeManagementSystem.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
