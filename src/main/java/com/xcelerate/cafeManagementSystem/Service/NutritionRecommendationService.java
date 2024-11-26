package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import com.xcelerate.cafeManagementSystem.Model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NutritionRecommendationService implements RecommendationStrategy {

    private final ProductService productService;
    private final NutritionAnalysisService nutritionAnalysisService;

    public NutritionRecommendationService(NutritionAnalysisService nutritionAnalysisService,
                                          ProductService productService) {
        this.nutritionAnalysisService = nutritionAnalysisService;
        this.productService = productService;
    }

    @Override
    public List<ProductDTO> getRecommendations(String prompt) {
        List<ProductDTO> products = productService.getAllProducts();
        StringBuilder productItems = new StringBuilder();
        for (ProductDTO product : products) {
            productItems.append(product.getId()).append(". ");
            productItems.append(product.getName()).append(": ");
            product.getIngredients().forEach(ingredient ->
                    productItems.append(ingredient.getName()).append(", ")
            );
            // Remove the last comma and space
            if(product.getIngredients().size() > 0) {
                productItems.setLength(productItems.length() - 2);
            }
            productItems.append(".\n");
        }

        List<Long> productIds = nutritionAnalysisService.analyzeNutrition(productItems.toString(), prompt);

        for (Long productId : productIds) {
            System.out.println(productId);
        }

        return productService.getAllbyIds(productIds);

    }
}
