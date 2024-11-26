package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmotionRecommendationService implements RecommendationStrategy {
    private final EmotionAnalysisService emotionAnalysisService;

    private final ProductService productService;

    public EmotionRecommendationService(EmotionAnalysisService emotionAnalysisService,
                                          ProductService productService) {
        this.emotionAnalysisService = emotionAnalysisService;
        this.productService = productService;
    }

    @Override
    public List<ProductDTO> getRecommendations(String prompt) {
        String emotion = emotionAnalysisService.getEmotion(prompt);
        return productService.getAllByEmotion(emotion);
    }
}