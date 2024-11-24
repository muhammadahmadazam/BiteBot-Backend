package com.xcelerate.cafeManagementSystem.Controller;

import com.cloudinary.Api;
import com.xcelerate.cafeManagementSystem.DTOs.*;
import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.Order;
import com.xcelerate.cafeManagementSystem.Model.Product;
import com.xcelerate.cafeManagementSystem.Model.SalesLineItem;
import com.xcelerate.cafeManagementSystem.Service.CustomerService;
import com.xcelerate.cafeManagementSystem.Service.OrderService;
import com.xcelerate.cafeManagementSystem.Service.OtpService;
import com.xcelerate.cafeManagementSystem.Service.ProductService;
import com.xcelerate.cafeManagementSystem.Utils.JwtUtil;
import jakarta.validation.constraints.Past;
import org.apache.hc.client5.http.auth.BearerToken;
import org.aspectj.weaver.ast.Or;
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



    private final OrderService orderService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final OtpService otpService;

    public OrderController(OrderService orderService, ProductService productService, CustomerService customerService, OtpService otpService) {

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
        order.setSector(orderRequest.getSector());
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
    public ResponseEntity<ApiResponseDTO<String>> placeOrder(@RequestBody otpDTO otp) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Customer c =customerService.getCustomerByEmail(email);
        if (c == null) {
            ApiResponseDTO<String> apiResponseDTO = new ApiResponseDTO<String>();
            apiResponseDTO.message = "User not found, cannot place order";
            return new ResponseEntity<ApiResponseDTO<String>>(apiResponseDTO, HttpStatus.BAD_REQUEST);
        }

        if (otpService.validateOtp(email, otp.otp)) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Order confirmed successfully", email);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }else{
            ApiResponseDTO<String> responseDTO = new ApiResponseDTO<>();
            responseDTO.message = "OTP verification failed";
            return new ResponseEntity<>(responseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/order/resendOTP")
    public ResponseEntity<ApiResponseDTO<String>>  resendOTP() {
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

    @GetMapping("/orders/track")
    public ResponseEntity<ApiResponseDTO<List<OrderDTO>>> trackOrder(){
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        System.out.println("Email: ");
        System.out.println("Email: " + email);
        Customer c = customerService.getCustomerByEmail(email);
        if (c == null) {
            ApiResponseDTO<List<OrderDTO>> apiResponseDTO = new ApiResponseDTO<>();
            apiResponseDTO.message = "User not found, cannot place order";
            return new ResponseEntity<>(apiResponseDTO, HttpStatus.BAD_REQUEST);
        }

        List<Order> orders = orderService.getConfirmedOrdersByCustomerId(c.getId());

        List<OrderDTO> orders_details = new ArrayList<>();

        for (Order order : orders) {
            List<SalesLineItemDTO> salesLineItems = new ArrayList<>();
            for (SalesLineItem salesLineItem : order.getOrderItems()) {

                SalesLineItemDTO salesLineItemDTO = new SalesLineItemDTO(salesLineItem.getQuantity(), salesLineItem.getProduct().getName(), salesLineItem.getProduct().getImageLink(), salesLineItem.getUnitPrice());
                salesLineItems.add(salesLineItemDTO);
            }
            String estimated_time = orderService.getEstimatedTime(order.getLatitude(), order.getLongitude());
            OrderDTO orderDTO = new OrderDTO(order.getOrderId(), order.getTotalPrice(), order.getCustomer().getId(), order.getStatus(), order.getOrderDate(),estimated_time, order.getPaymentMethod(), order.getAddress(), salesLineItems);
            orders_details.add(orderDTO);
        }

        ApiResponseDTO<List<OrderDTO>> response = new ApiResponseDTO<>("orders fetched successfully",orders_details);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/orders/get-by-customer")
    public ResponseEntity<ApiResponseDTO<List<PastOrderDTO>>> getPastOrders(){
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Customer c = customerService.getCustomerByEmail(email);
        if (c == null) {
            ApiResponseDTO<List<PastOrderDTO>> apiResponseDTO = new ApiResponseDTO<>();
            apiResponseDTO.message = "User not found, cannot place order";

            return new ResponseEntity<>(apiResponseDTO, HttpStatus.BAD_REQUEST);
        }

        List<Order> orders = orderService.getConfirmedOrdersByCustomerId(c.getId());

        List<PastOrderDTO> orders_details = new ArrayList<>();

        for (Order order : orders) {
            List<SalesLineItemDTO> salesLineItems = new ArrayList<>();
            for(SalesLineItem salesLineItem : order.getOrderItems()) {
                SalesLineItemDTO salesLineItemDTO = new SalesLineItemDTO(salesLineItem.getQuantity(), salesLineItem.getProduct().getName(), salesLineItem.getProduct().getImageLink(), salesLineItem.getUnitPrice());
                salesLineItems.add(salesLineItemDTO);
            }
            PastOrderDTO pastOrderDTO = new PastOrderDTO(order.getOrderId(), order.getTotalPrice(), order.getStatus(), order.getOrderDate(), order.getPaymentMethod(), order.getAddress(), salesLineItems);
            orders_details.add(pastOrderDTO);
        }

        ApiResponseDTO<List<PastOrderDTO>> response = new ApiResponseDTO<>("orders fetched successfully",orders_details);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}



