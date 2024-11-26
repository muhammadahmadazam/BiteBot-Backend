package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecommendationService {
    private final RecommendationContext recommendationContext;
    private final EmotionRecommendationService emotionalRecommendationService;
    private final NutritionRecommendationService nutritionalRecommendationService;

    @Autowired
    public RecommendationService(RecommendationContext recommendationContext,
                                 EmotionRecommendationService emotionalRecommendationService,
                                 NutritionRecommendationService nutritionalRecommendationService) {
        this.recommendationContext = recommendationContext;
        this.emotionalRecommendationService = emotionalRecommendationService;
        this.nutritionalRecommendationService = nutritionalRecommendationService;
    }

    public List<ProductDTO> getRecommendations(String type, String prompt) {
        if ("emotional".equalsIgnoreCase(type)) {
            recommendationContext.setStrategy(emotionalRecommendationService);
        } else if ("nutritional".equalsIgnoreCase(type)) {
            recommendationContext.setStrategy(nutritionalRecommendationService);
        } else {
            return null;
        }
        return recommendationContext.getRecommendations(prompt);
    }
}