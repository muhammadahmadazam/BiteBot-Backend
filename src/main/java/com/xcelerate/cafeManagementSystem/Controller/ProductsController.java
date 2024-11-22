package com.xcelerate.cafeManagementSystem.Controller;


import com.xcelerate.cafeManagementSystem.DTOs.EmailDTO;
import com.xcelerate.cafeManagementSystem.DTOs.ProductDTO;
import com.xcelerate.cafeManagementSystem.DTOs.PromptDTO;
import com.xcelerate.cafeManagementSystem.Model.Product;
import com.xcelerate.cafeManagementSystem.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@CrossOrigin(origins = "${frontendURL}")
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

    @GetMapping("/products/get/all")
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new ResponseEntity<List<ProductDTO>>(products, HttpStatus.OK);
    }

}
