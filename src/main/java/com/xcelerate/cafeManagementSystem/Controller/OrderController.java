package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.ApiResponseDTO;
import com.xcelerate.cafeManagementSystem.DTOs.OrderRequest;
import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.Order;
import com.xcelerate.cafeManagementSystem.Model.Product;
import com.xcelerate.cafeManagementSystem.Model.SalesLineItem;
import com.xcelerate.cafeManagementSystem.Service.CustomerService;
import com.xcelerate.cafeManagementSystem.Service.OrderService;
import com.xcelerate.cafeManagementSystem.Service.OtpService;
import com.xcelerate.cafeManagementSystem.Service.ProductService;
import com.xcelerate.cafeManagementSystem.Utils.JwtUtil;
import org.apache.hc.client5.http.auth.BearerToken;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.security.oauth2.jwt.Jwt;
@Controller
@CrossOrigin(origins = "${frontendURL}")
@RequestMapping("/api/")
public class OrderController {


    private final JwtUtil jwtUtil;
    private final OrderService orderService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final OtpService otpService;

    public OrderController(JwtUtil jwtUtil, OrderService orderService, ProductService productService, CustomerService customerService, OtpService otpService) {
        this.jwtUtil = jwtUtil;
        this.orderService = orderService;
        this.productService = productService;
        this.customerService = customerService;
        this.otpService = otpService;
    }

    @PostMapping ("/order/create")
    public ResponseEntity<ApiResponseDTO<String >> createOrder(@RequestBody OrderRequest orderRequest) {

        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        /*  CREATE ORDER OBJECT AND PASS TO THE ORDER SERVICE */
        Order order = new Order();
        Customer c =customerService.getCustomerByEmail(email);
        if (c == null) {
            ApiResponseDTO<String> apiResponseDTO = new ApiResponseDTO<String>();
            apiResponseDTO.message = "User not found, cannot place order";
            return new ResponseEntity<ApiResponseDTO<String>>(apiResponseDTO, HttpStatus.BAD_REQUEST);
        }
        order.setCustomer(c);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setAddress(orderRequest.getAddress());
        order.setLatitude(orderRequest.getLatitude());
        order.setLongitude(orderRequest.getLongitude());
        List<SalesLineItem> saleLineItems = new ArrayList<>();

        for (OrderRequest.LineItemRequest lineItem : orderRequest.getLineItems()) {
            SalesLineItem salesLineItem = new SalesLineItem();
            Product p = productService.getProductById(lineItem.getProductId());
            if (p == null) {
                System.out.println("LineItem ID: " + lineItem.getProductId());
                ApiResponseDTO<String> apiResponseDTO = new ApiResponseDTO<String>();
                apiResponseDTO.message = "Product not found";
                return new ResponseEntity<ApiResponseDTO<String>>(apiResponseDTO, HttpStatus.BAD_REQUEST);
            }
            salesLineItem.setProduct(p);
            salesLineItem.setQuantity((long)lineItem.getQuantity());
            salesLineItem.setUnitPrice((long)p.getPrice());
            salesLineItem.setOrder(order);
            saleLineItems.add(salesLineItem);
        }

        order.setOrderItems(saleLineItems);

        if (orderService.createOrder(order)) {
            ApiResponseDTO<String> responseDTO = new ApiResponseDTO<>();
            responseDTO.message = "Order created";
            boolean isOtpSent = otpService.generateAndSendOtp(email);
            return new ResponseEntity<ApiResponseDTO<String>>(responseDTO, HttpStatus.CREATED);
        }else{
            ApiResponseDTO<String> responseDTO = new ApiResponseDTO<>();
            responseDTO.message = "Unable to place order";
            return new ResponseEntity<ApiResponseDTO<String>>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/order/confirm")
    public ResponseEntity<ApiResponseDTO<String>> placeOrder(@RequestBody String otp) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer c =customerService.getCustomerByEmail(email);
        if (c == null) {
            ApiResponseDTO<String> apiResponseDTO = new ApiResponseDTO<String>();
            apiResponseDTO.message = "User not found, cannot place order";
            return new ResponseEntity<ApiResponseDTO<String>>(apiResponseDTO, HttpStatus.BAD_REQUEST);
        }

        if (otpService.validateOtp(email, otp)) {
            customerService.verifyUser(email);
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Email verified successfully", email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            ApiResponseDTO<String> responseDTO = new ApiResponseDTO<>();
            responseDTO.message = "OTP verification failed";
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/order/resendOTP")
    public ResponseEntity<ApiResponseDTO<String>>  resendOTP(@RequestBody String otp) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        boolean isOtpSent = otpService.generateAndSendOtp(email);
        if (isOtpSent) {
            ApiResponseDTO<String> responseDTO = new ApiResponseDTO<>();
            responseDTO.message = "OTP resent successfully";
            return new ResponseEntity<>( responseDTO, HttpStatus.OK);
        }else{
            ApiResponseDTO<String> responseDTO = new ApiResponseDTO<>();
            responseDTO.message = "Failed to resend OTP";
            return new ResponseEntity<>( responseDTO, HttpStatus.BAD_REQUEST);
        }
    }

}



