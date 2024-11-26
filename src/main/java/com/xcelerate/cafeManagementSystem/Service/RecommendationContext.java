package com.xcelerate.cafeManagementSystem.Service;


import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import com.xcelerate.cafeManagementSystem.Service.RecommendationStrategy;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecommendationContext {
    private RecommendationStrategy strategy;

    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public List<com.xcelerate.cafeManagementSystem.DTOs.ProductDTO> getRecommendations(String prompt) {
        return strategy.getRecommendations(prompt);
    }
}