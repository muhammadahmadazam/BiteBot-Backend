package com.xcelerate.cafeManagementSystem.Controller;


import com.xcelerate.cafeManagementSystem.DTOs.ApiResponseDTO;
import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import com.xcelerate.cafeManagementSystem.DTOs.PromptDTO;
import com.xcelerate.cafeManagementSystem.Model.Product;
import com.xcelerate.cafeManagementSystem.Service.EmotionAnalysisService;
import com.xcelerate.cafeManagementSystem.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendation/")
@CrossOrigin(origins = "${frontendURL}")
public class RecommendationController {
    @Autowired
    private EmotionAnalysisService emotionAnalysisService;

    @Autowired
    private ProductService productService;

    @PostMapping("/emotional")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> getRecommendationByEmotion(@RequestBody PromptDTO promptDTO) {

        if(promptDTO.text == null || promptDTO.text.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("Text cannot be empty", null));
        }

        String emotion = emotionAnalysisService.getEmotion(promptDTO.text);

        if(emotion == null) {
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("Error: Emotion not found", null));
        }

        List<ProductDTO> products = productService.getAllByEmotion(emotion);

        return ResponseEntity.ok(new ApiResponseDTO<>("Recommendations Successfully Fetched", products));
    }


}
