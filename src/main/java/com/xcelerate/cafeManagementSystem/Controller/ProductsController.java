package com.xcelerate.cafeManagementSystem.Controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xcelerate.cafeManagementSystem.DTOs.ApiResponseDTO;
import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import com.xcelerate.cafeManagementSystem.DTOs.Product_Delete_DTO;
import com.xcelerate.cafeManagementSystem.Model.Product;
import com.xcelerate.cafeManagementSystem.Service.CloudinaryService;
import com.xcelerate.cafeManagementSystem.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/")
public class ProductsController {

    @Autowired
    ProductService productService;

    @Autowired
    CloudinaryService cloudinaryService;

    @PostMapping("/product/create")
    public ResponseEntity<ApiResponseDTO<ProductDTO>> createProduct( @RequestParam("image") MultipartFile file,
                                                 @RequestParam("product") String productJson) {
        ApiResponseDTO<ProductDTO> apiResponseDTO = new ApiResponseDTO<>();

        try {
            // Deserialize the product JSON into a Product object
            ObjectMapper objectMapper = new ObjectMapper();
            Product p = objectMapper.readValue(productJson, Product.class);

            // Handle the image upload
            String uploadedImageUrl = cloudinaryService.uploadImage(file);

            // Set the uploaded image URL to the product
            p.setImageLink(uploadedImageUrl);


            Product newP = productService.createProduct(p);
            if (p != null) {
                ProductDTO newProductDTO = new ProductDTO(newP);
                apiResponseDTO.data = newProductDTO;
                apiResponseDTO.message="Product created successfully.";
                return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
            }else{
                apiResponseDTO.message = "ERROR: Unable to create product.";
                return new ResponseEntity<>(apiResponseDTO, HttpStatus.NOT_ACCEPTABLE);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


    }


    @PostMapping("/product/update")
    public ResponseEntity<ApiResponseDTO<String>> updateProduct(@RequestBody @Valid Product p) {
        ApiResponseDTO<String> responseDTO = new ApiResponseDTO<>();
        if (productService.updateProduct(p)) {
            responseDTO.message = "Product updated successfully.";
            responseDTO.data = "PRODUCT_UPDATED";
            return new ResponseEntity<>(responseDTO, HttpStatus.OK);
        }else{
            responseDTO.message = "ERROR: Unable to update product.";
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/product/delete")
    public ResponseEntity<ApiResponseDTO<String>> deleteProduct(@RequestBody @Valid Product_Delete_DTO p) {
        ApiResponseDTO<String> response = new ApiResponseDTO<>();
        if (productService.deleteById(p.getProductId())){
            response.message = "Product deleted successfully.";
            response.data = "PRODUCT_DELETED";
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            response.message = "Failed to delete product.";
            response.data = "";
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/products/get/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<List<ProductDTO>>(products, HttpStatus.OK);
    }

    @GetMapping("/product/get/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        ProductDTO product = productService.getByProductById(id);
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<ProductDTO>(product, HttpStatus.OK);
        }
    }
}
