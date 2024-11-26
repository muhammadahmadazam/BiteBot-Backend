package com.xcelerate.cafeManagementSystem.Controller;

import com.xcelerate.cafeManagementSystem.DTOs.*;
import com.xcelerate.cafeManagementSystem.Model.*;
import com.xcelerate.cafeManagementSystem.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Controller
@CrossOrigin(origins = "${frontendURL}")
@RequestMapping("/api/")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final CustomerService customerService;
    private final OtpService otpService;
    private final WorkerService workerService;

    public OrderController(OrderService orderService, ProductService productService, CustomerService customerService, OtpService otpService, WorkerService workerService) {
        this.orderService = orderService;
        this.productService = productService;
        this.customerService = customerService;
        this.otpService = otpService;
        this.workerService = workerService;
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

        System.out.println("Validating OTP...");
        if (otpService.validateOtp(email, otp.otp)) {
            System.out.println("Validated OTP");
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Order confirmed successfully", email);
            System.out.println("Confirming order using customer id: " + c.getId());
            if (orderService.confirmOrder(c.getId())){
                System.out.println("Order Status Updated");
                return new ResponseEntity<>(response, HttpStatus.OK);
            }else{
                System.out.println("Order Status not updated");
                response.message = "Order confirmation failed";
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        }else{
            ApiResponseDTO<String> responseDTO = new ApiResponseDTO<>();
            responseDTO.message = "OTP verification failed";
            return new ResponseEntity<>(responseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/order/resendOTP")
    public ResponseEntity<ApiResponseDTO<String>>  resendOTP() {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Customer c = customerService.getCustomerByEmail(email);
        if (c == null) {
            ApiResponseDTO<String> apiResponseDTO = new ApiResponseDTO<String>();
            apiResponseDTO.message = "User not found, cannot place order";
            return new ResponseEntity<ApiResponseDTO<String>>(apiResponseDTO, HttpStatus.BAD_REQUEST);
        }

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

        List<Order> orders = orderService.getDeliveredOrdersByCustomerId(c.getId());

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

    @GetMapping("/orders/get-processing-and-prepared")
    public ResponseEntity<ApiResponseDTO<Order_Worker_DTO>> getProcessingOrders(){
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Worker w = workerService.findByEmail(email);
        if (w == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{

            List<Order> orders=  orderService.getQueuedOrders();
            List<Order_DeliveryDTO> processingDTO = orders.stream().map(Order_DeliveryDTO::new).toList();

            List<Order> preparingOrders =  orderService.getPreparingOrders();
            List<Order_DeliveryDTO> preparingDTO = preparingOrders.stream().map(Order_DeliveryDTO::new).toList();

            Order_Worker_DTO orderWorkerDto = new Order_Worker_DTO();
            orderWorkerDto.processing=processingDTO;
            orderWorkerDto.preparing=preparingDTO;

            ApiResponseDTO<Order_Worker_DTO> apiResponseDTO = new ApiResponseDTO<>();
            apiResponseDTO.message = "Successfully fetched processing orders.";
            apiResponseDTO.data = orderWorkerDto;

            return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
        }
    }

    @PostMapping("/order/prepare")
    public ResponseEntity<ApiResponseDTO<String>> prepareOrder(@RequestBody Order_Update_DTO orderUpdateDto) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Worker w = workerService.findByEmail(email);
        if (w == null) {
            ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
            apiResponse.message = "You are not authorized to change order status.";
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }else{
            boolean orderStatusUpdated =  orderService.prepareOrder(orderUpdateDto.getOrderId());
            if (orderStatusUpdated) {
                ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
                apiResponse.message = "Sucessfully updated order status";
                apiResponse.data = "UPDATE_SUCCESSFUL";
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            }else{
                ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
                apiResponse.message = "Order Status not updated successfully.";
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
        }

    }

    @PostMapping("/order/complete")
    public ResponseEntity<ApiResponseDTO<String>> completeOrder(@RequestBody Order_Update_DTO orderUpdateDto) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Worker w = workerService.findByEmail(email);
        if (w == null) {
            ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
            apiResponse.message = "You are not authorized to change order status.";
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }else{
            boolean orderStatusUpdated =  orderService.completeOrder(orderUpdateDto.getOrderId());
            if (orderStatusUpdated) {
                ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
                apiResponse.message = "Order Status updated successfully.";
                apiResponse.data = "ORDER_COMPLETED";
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            }else{
                ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
                apiResponse.message = "Order Status not updated successfully.";
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
        }

    }


    @PostMapping("/order/delivered")
    public ResponseEntity<ApiResponseDTO<String>> deliverOrder(@RequestBody Order_Update_DTO orderUpdateDto) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Worker w = workerService.findByEmail(email);
        if (w == null) {
            ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
            apiResponse.message = "You are not authorized to change order status.";
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }else{
            boolean orderStatusUpdated =  orderService.deliverOrder(orderUpdateDto.getOrderId());
            if (orderStatusUpdated) {
                ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
                apiResponse.message = "Order Status updated successfully.";
                apiResponse.data = "ORDER_DELIVERED";
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            }else{
                ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
                apiResponse.message = "Order Status not updated successfully.";
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
        }

    }


    @PostMapping("/order/deliver-failed")
    public ResponseEntity<ApiResponseDTO<String>> deliverOrderFailed(@RequestBody Order_Failed_DTO orderUpdateDto) {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Worker w = workerService.findByEmail(email);
        if (w == null) {
            ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
            apiResponse.message = "You are not authorized to change order status.";
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        }else{
            boolean orderStatusUpdated =  orderService.deliverFailed(orderUpdateDto.getOrderId(), orderUpdateDto.getReason(), w.getId());
            if (orderStatusUpdated) {
                ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
                apiResponse.message = "Order Status updated successfully.";
                apiResponse.data = "ORDER_UPDATED";
                return new ResponseEntity<>(apiResponse, HttpStatus.OK);
            }else{
                ApiResponseDTO<String> apiResponse = new ApiResponseDTO<String>();
                apiResponse.message = "Order Status not updated successfully.";
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
        }

    }


    @PostMapping("/orders/get-prepared-orders")
    public ResponseEntity<ApiResponseDTO<List<Order_DeliveryDTO>>> getUndeliveredOrders() {
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Worker w = workerService.findByEmail(email);
        if (w == null) {
            ApiResponseDTO<List<Order_DeliveryDTO>> apiResponseDTO = new ApiResponseDTO<>();
            apiResponseDTO.message = "You are not authorized to change order status.";
            return new ResponseEntity<>(apiResponseDTO, HttpStatus.UNAUTHORIZED);
        }else{
             List<Order> orders=  orderService.getCompleteOrders();
             ApiResponseDTO<List<Order_DeliveryDTO>> apiResponseDTO = new ApiResponseDTO<>();
             List<Order_DeliveryDTO> deliveryDTO = orders.stream().map(Order_DeliveryDTO::new).toList();
             apiResponseDTO.message = "Successfully fetched orders to be delivered.";
             apiResponseDTO.data = deliveryDTO;
             return new ResponseEntity<>(apiResponseDTO, HttpStatus.OK);
        }
    }

}