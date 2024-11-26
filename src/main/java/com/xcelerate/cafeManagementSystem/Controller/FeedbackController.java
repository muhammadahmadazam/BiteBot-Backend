package com.xcelerate.cafeManagementSystem.Controller;


import com.xcelerate.cafeManagementSystem.DTOs.*;
import com.xcelerate.cafeManagementSystem.Model.Customer;
import com.xcelerate.cafeManagementSystem.Model.Feedback;
import com.xcelerate.cafeManagementSystem.Model.Order;
import com.xcelerate.cafeManagementSystem.Model.SalesLineItem;
import com.xcelerate.cafeManagementSystem.Service.CustomerService;
import com.xcelerate.cafeManagementSystem.Service.EmailService;
import com.xcelerate.cafeManagementSystem.Service.FeedbackService;
import com.xcelerate.cafeManagementSystem.Service.OrderService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/feedback/")
@CrossOrigin(origins = "${frontendURL}")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<ApiResponseDTO<String>> createFeedback(@RequestBody CreateFeedbackDTO createFeedbackDTO) {
        String content = createFeedbackDTO.content;
        String order_id = createFeedbackDTO.order_id;
        if (content == null || order_id == null) {
            return new ResponseEntity<>(new ApiResponseDTO<>("Invalid request", null), HttpStatus.BAD_REQUEST);
        }
        Order order = orderService.findByOrderId(order_id);
        if (order == null) {
            return new ResponseEntity<>(new ApiResponseDTO<>("Invalid order id", null), HttpStatus.BAD_REQUEST);
        }
        Feedback feedback = feedbackService.createFeedback(content, order);
        if (feedback != null) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Feedback created", "Feedback id: " + feedback.feedback_id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponseDTO<String> response = new ApiResponseDTO<>("Invalid order id", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponseDTO<List<FeedbackDTO>>> getFeedback() {
        List<Feedback> feedbacks = feedbackService.getFeedbacks();
        List<FeedbackDTO> feedback_details = new ArrayList<>();
        for (Feedback feedback : feedbacks) {
            List<SalesLineItemDTO> items = new ArrayList<>();
            for (SalesLineItem item : feedback.order.getOrderItems()) {
                items.add(new SalesLineItemDTO(item.getQuantity(), item.getProduct().getName(),item.getProduct().getImageLink(), item.getUnitPrice()));
            }
            PastOrderDTO order = new PastOrderDTO(feedback.order.getOrderId(), feedback.order.getTotalPrice(), feedback.order.getStatus(), feedback.order.getOrderDate(), feedback.order.getPaymentMethod(), feedback.order.getAddress(), items);
            feedback_details.add(new FeedbackDTO(feedback.feedback_id, feedback.content, feedback.emotion, order, feedback.order.customer.getName(), feedback.order.customer.getId()));
        }

        if (feedbacks != null) {
            ApiResponseDTO<List<FeedbackDTO>> response = new ApiResponseDTO<>("Feedback found", feedback_details);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponseDTO<List<FeedbackDTO>> response = new ApiResponseDTO<>("Feedback not found", null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @PostMapping("/send/follow-up-email")
    public ResponseEntity<ApiResponseDTO<String>> sendFollowUpEmail(@RequestBody FollowUpEmailDTO sendFollowUpEmailDTO) {
        long customer_id = sendFollowUpEmailDTO.customer_id;
        String feedback_id = sendFollowUpEmailDTO.feedback_id;
        String order_id = sendFollowUpEmailDTO.order_id;
        if (feedback_id == null) {
            return new ResponseEntity<>(new ApiResponseDTO<>("Invalid request", null), HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerService.getCustomerById(customer_id);
        if (customer == null) {
            return new ResponseEntity<>(new ApiResponseDTO<>("Invalid customer id", null), HttpStatus.BAD_REQUEST);
        }
        String email = customer.getEmail();
        boolean status = sendFollowUpEmail(email, sendFollowUpEmailDTO.body, order_id);
        if (status) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Email sent", null);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponseDTO<String> response = new ApiResponseDTO<>("Invalid feedback id", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private boolean sendFollowUpEmail(String email, String body, String order_id) {
        try{
            emailService.sendEmail(email, "Feedback Followup for order no " + order_id, body);
            return true;
        } catch (Exception e ) {
            return false;
        }

    }

}
