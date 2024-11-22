package com.xcelerate.cafeManagementSystem.Repository;
import com.xcelerate.cafeManagementSystem.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> getAllByEmotion(String emotion);

    @Query("SELECT p FROM Product p JOIN FETCH p.ingredients WHERE p.id = :id")
    Optional<Product> findByIdWithIngredients(@Param("id") int id);


}
