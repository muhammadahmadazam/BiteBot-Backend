package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;

import java.util.List;

public interface RecommendationStrategy{

    // as no of parametres can be different
    public List<ProductDTO> getRecommendations(String prompt);
}

