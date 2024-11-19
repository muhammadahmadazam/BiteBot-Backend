package com.xcelerate.cafeManagementSystem.Repository;

import com.xcelerate.cafeManagementSystem.Model.Ingredient;
import com.xcelerate.cafeManagementSystem.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>  {
    Optional<Ingredient> findByName(String name);
}
