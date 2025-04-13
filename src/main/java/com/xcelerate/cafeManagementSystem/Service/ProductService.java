package com.xcelerate.cafeManagementSystem.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import com.xcelerate.cafeManagementSystem.DTOs.Product_Delete_DTO;
import com.xcelerate.cafeManagementSystem.Model.Ingredient;
import com.xcelerate.cafeManagementSystem.Model.Product;
import com.xcelerate.cafeManagementSystem.Repository.IngredientRepository;
import com.xcelerate.cafeManagementSystem.Repository.ProductRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final IngredientRepository ingredientRepository;


    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)  // Timeout for establishing connection
            .readTimeout(30, TimeUnit.SECONDS)     // Timeout for reading response
            .writeTimeout(30, TimeUnit.SECONDS)    // Timeout for writing request
            .build();
    private final Gson gson = new Gson(); // Reuse a single Gson instance

    @Value("${chatApiURL}")
    private String chatApiURL;

    @Autowired
    public ProductService(ProductRepository productRepository, IngredientRepository ingredientRepository) {
        this.productRepository = productRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(Product p) {
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

            Product savedProduct = productRepository.save(p);
            System.out.println(savedProduct.getId());
            System.out.println(savedProduct.getDescription());
            System.out.println(savedProduct.getName());
//            System.out.println(savedProduct.getId());

            Map<String, Object> body = new HashMap<>();
            body.put("item_id", savedProduct.getId());
            body.put("name", savedProduct.getName());
            body.put("description", savedProduct.getDescription());
            // Convert ingredients to list of strings
            List<String> ingredientNames = savedProduct.getIngredients().stream()
                    .map(Ingredient::getName)
                    .collect(Collectors.toList());
            body.put("ingredients", ingredientNames);
//            body.put("ingredients", savedProduct.getIngredients().stream().map(in -> in.getName()));

            String jsonStr = gson.toJson(body);
            okhttp3.RequestBody requestBody = RequestBody.create(jsonStr, okhttp3.MediaType.parse("application/json"));

            Request request = new Request.Builder()
                    .url(chatApiURL + "/items/add")
                    .post(requestBody)
                    .build();

            try (okhttp3.Response response = client.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    System.out.println("Request successful: " + response.code());
                    JsonObject jsonObject = gson.fromJson(response.body().string(), JsonObject.class);
                    Type listType = new TypeToken<List<Long>>() {}.getType();
//                    return gson.fromJson(jsonObject.get("response"), listType);
                    return savedProduct;
                } else {
                    System.out.println("Request failed: " + response.code());
                    productRepository.delete(savedProduct);
                    throw new Exception("Failed to add product to vector store.");
                }
            } catch (IOException e) {
                productRepository.delete(savedProduct);
                e.printStackTrace();
            }

            return p;
        }catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Transactional(rollbackFor = Exception.class)
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
            productDTO.setEmotion(product.getEmotion());
            productDTO.setIngredients(product.getIngredients().stream()
                    .map(ingredient -> new ProductDTO.IngredientDTO(ingredient.getId(), ingredient.getName()))
                    .collect(Collectors.toSet()));
            return productDTO;
        }).collect(Collectors.toList());

    }

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    public ProductDTO getByProductById(long id) {
        Optional<Product> product = productRepository.findByIdWithIngredients((int)id);

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


    @Transactional(rollbackFor = Exception.class)
    public boolean updateProduct(Product p) {
        Product orgProduct = productRepository.findById(p.getId()).orElse(null);
        if (orgProduct != null) {
            orgProduct.setName(p.getName());
            orgProduct.setDescription(p.getDescription());
            orgProduct.setAvailability(p.isAvailability());
            orgProduct.setPrice(p.getPrice());
            orgProduct.setCategory(p.getCategory());
            orgProduct.setImageLink(p.getImageLink());
            orgProduct.setEmotion(p.getEmotion());
            productRepository.save(orgProduct);
            return true;
        }else{
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(long id) {
        Product p = productRepository.findById(id).orElse(null);
        if (p != null) {
            productRepository.delete(p);
            return true;
        }else{
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public List<ProductDTO> getAllbyIds(List<Long> ids) {
        if(ids == null || ids.isEmpty()){
            return new ArrayList<>();
        }
        List<Product> products = productRepository.findAllById(ids);
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
            productDTO.setEmotion(product.getEmotion());
            productDTO.setIngredients(product.getIngredients().stream()
                    .map(ingredient -> new ProductDTO.IngredientDTO(ingredient.getId(), ingredient.getName()))
                    .collect(Collectors.toSet()));
            return productDTO;
        }).collect(Collectors.toList());
    }
}




