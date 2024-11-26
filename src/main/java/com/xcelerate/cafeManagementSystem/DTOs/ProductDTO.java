package com.xcelerate.cafeManagementSystem.DTOs;
import com.xcelerate.cafeManagementSystem.Model.Product;
import jakarta.persistence.Column;

import java.util.Set;
import java.util.stream.Collectors;

public class ProductDTO {
    private long id;
    protected String name;
    protected float price;
    protected String category;
    protected boolean availability;
    protected String imageLink;
    protected String description;
    private Set<IngredientDTO> ingredients;
    private String emotion;
    // Getters and setters

    public  ProductDTO() {}
    public ProductDTO(Product product) {
        this.setId(product.getId());
        this.setName(product.getName());
        this.setDescription(product.getDescription());
        this.setAvailability(product.isAvailability());
        this.setPrice(product.getPrice());
        this.setCategory(product.getCategory());
        this.setImageLink(product.getImageLink());
        this.setIngredients(product.getIngredients().stream()
                .map(ingredient -> new ProductDTO.IngredientDTO(ingredient.getId(), ingredient.getName()))
                .collect(Collectors.toSet()));
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<IngredientDTO> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Set<IngredientDTO> ingredients) {
        this.ingredients = ingredients;
    }

    public String getEmotion() {
        return emotion;
    }

    public void setEmotion(String emotion) {
        this.emotion = emotion;
    }

    public static class IngredientDTO {
        private Long id;
        private String name;

        // Constructor, Getters and Setters
        public IngredientDTO(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
