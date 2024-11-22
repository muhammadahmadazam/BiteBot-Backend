package com.xcelerate.cafeManagementSystem.Repository;
import com.xcelerate.cafeManagementSystem.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByEmotion(String emotion);
}
