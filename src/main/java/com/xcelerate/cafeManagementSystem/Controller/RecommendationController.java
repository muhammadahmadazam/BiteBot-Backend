package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.ApiResponseDTO;
import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import com.xcelerate.cafeManagementSystem.DTOs.PromptDTO;
import com.xcelerate.cafeManagementSystem.Service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/recommendation/")
@CrossOrigin(origins = "${frontendURL}")
public class RecommendationController {
    private static final Logger logger = Logger.getLogger(RecommendationController.class.getName());

    @Autowired
    private RecommendationService recommendationService;

    @PostMapping("/recommend")
    public ResponseEntity<ApiResponseDTO<List<ProductDTO>>> getRecommendation(@RequestBody PromptDTO promptDTO) {
        logger.info("Received request with prompt: " + promptDTO);

        if (promptDTO.getText() == null || promptDTO.getText().isEmpty() || promptDTO.getType() == null) {
            logger.warning("Invalid request: Text or Type is empty");
            return ResponseEntity.badRequest().body(new ApiResponseDTO<>("Text cannot be empty", null));
        }

        List<ProductDTO> products = recommendationService.getRecommendations(promptDTO.getType(), promptDTO.getText());
        if (products == null || products.isEmpty()) {
            logger.warning("No recommendations found");
            return ResponseEntity.noContent().build();
        }

        logger.info("Recommendations successfully fetched");
        return ResponseEntity.ok(new ApiResponseDTO<>("Recommendations Successfully Fetched", products));
    }
}