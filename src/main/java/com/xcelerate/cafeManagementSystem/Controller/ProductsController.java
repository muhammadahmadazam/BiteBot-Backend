package com.xcelerate.cafeManagementSystem.Controller;


import com.cloudinary.api.ApiResponse;
import com.xcelerate.cafeManagementSystem.DTOs.ApiResponseDTO;
import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import com.xcelerate.cafeManagementSystem.DTOs.Product_Delete_DTO;
import com.xcelerate.cafeManagementSystem.Model.Product;
import com.xcelerate.cafeManagementSystem.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class ProductsController {

    @Autowired
    ProductService productService;

    @PostMapping("/product/create")
    public ResponseEntity<String> createProduct(@RequestBody Product p) {
        boolean isCreated = productService.createProduct(p);
        if (isCreated) {
            return new ResponseEntity<>("Product created successfully.", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("ERROR: Unable to create product.", HttpStatus.NOT_ACCEPTABLE);
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
