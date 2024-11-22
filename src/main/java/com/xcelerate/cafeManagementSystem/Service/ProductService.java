package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import com.xcelerate.cafeManagementSystem.Model.Ingredient;
import com.xcelerate.cafeManagementSystem.Model.Product;
import com.xcelerate.cafeManagementSystem.Repository.IngredientRepository;
import com.xcelerate.cafeManagementSystem.Repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;


    @Autowired
    public ProductService(ProductRepository productRepository, IngredientRepository ingredientRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean createProduct(Product p) {
        try {
//            //  CHECK IF ALL INGREDIENTS ARE THERE IN THE TABLE IF NOT INSERT THEM FIRST
            List<Ingredient> newIngredients = new ArrayList<>();
            for (Ingredient i : p.getIngredients()) {

                Optional<Ingredient> ingredient = ingredientRepository.findByName(i.getName());
                if (ingredient.isEmpty()) {
                        // If it doesn't exist, create a new ingredient
                        Ingredient newIngredient = new Ingredient();
                        newIngredient.setName(i.getName());
                        System.out.println("Ingredient made...");
                        ingredientRepository.save(newIngredient);
                        newIngredients.add(newIngredient);
                }else{
                    newIngredients.add(ingredient.get());
                }
            }
            for (Ingredient i : p.getIngredients()) {
                System.out.println( i.getId() +  " " + i.getName());
            }
            for (Ingredient i : newIngredients) {
                System.out.println( i.getId() +  " " + i.getName());
            }
            p.setIngredients(new HashSet<>(Arrays.asList(newIngredients.toArray(new Ingredient[0]))));
//            System.out.println("Ingredients are okay..");
            productRepository.save(p);
            return true;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public List<ProductDTO> getAllProducts() {

        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> {
            // Map Product to ProductDTO
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setAvailability(product.isAvailability());
            productDTO.setPrice(product.getPrice());
            productDTO.setCategory(product.getCategory());
            productDTO.setImageLink(product.getImageLink());

            productDTO.setIngredients(product.getIngredients().stream()
                    .map(ingredient -> new ProductDTO.IngredientDTO(ingredient.getId(), ingredient.getName()))
                    .collect(Collectors.toSet()));
            return productDTO;
        }).collect(Collectors.toList());

    }

    public List<ProductDTO> getAllByEmotion(String emotion) {
        List<Product> products = productRepository.getAllByEmotion(emotion);
        return products.stream().map(product -> {
            // Map Product to ProductDTO
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setAvailability(product.isAvailability());
            productDTO.setPrice(product.getPrice());
            productDTO.setCategory(product.getCategory());
            productDTO.setImageLink(product.getImageLink());

            productDTO.setIngredients(product.getIngredients().stream()
                    .map(ingredient -> new ProductDTO.IngredientDTO(ingredient.getId(), ingredient.getName()))
                    .collect(Collectors.toSet()));
            return productDTO;
        }).collect(Collectors.toList());
    }

    public Product getProductById(long id) {
        Optional<Product> p = productRepository.findById(id);
        return p.orElse(null);
    }

    public ProductDTO getByProductById(long id) {
        Optional<Product> product = productRepository.findByIdWithIngredients(id);

        if (product.isPresent()) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.get().getId());
            productDTO.setName(product.get().getName());
            productDTO.setDescription(product.get().getDescription());
            productDTO.setAvailability(product.get().isAvailability());
            productDTO.setPrice(product.get().getPrice());
            productDTO.setCategory(product.get().getCategory());
            productDTO.setImageLink(product.get().getImageLink());
            productDTO.setIngredients(product.get().getIngredients().stream()
                    .map(ingredient -> new ProductDTO.IngredientDTO(ingredient.getId(), ingredient.getName()))
                    .collect(Collectors.toSet()));
            return productDTO;
        }else{
            return null;
        }
    }
}




