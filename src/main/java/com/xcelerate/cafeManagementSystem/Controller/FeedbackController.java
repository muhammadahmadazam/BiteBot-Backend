package com.xcelerate.cafeManagementSystem.Controller;


import com.xcelerate.cafeManagementSystem.DTOs.ApiResponseDTO;
import com.xcelerate.cafeManagementSystem.DTOs.CreateFeedbackDTO;
import com.xcelerate.cafeManagementSystem.Model.Feedback;
import com.xcelerate.cafeManagementSystem.Service.FeedbackService;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback/")
@CrossOrigin(origins = "${frontendURL}")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;


    @PostMapping("/order")
    public ResponseEntity<ApiResponseDTO<String>> createFeedback(@RequestBody CreateFeedbackDTO createFeedbackDTO) {
        String content = createFeedbackDTO.content;
        String order_id = createFeedbackDTO.order_id;
        if(content == null || order_id == null) {
            return new ResponseEntity<>(new ApiResponseDTO<>("Invalid request", null), HttpStatus.BAD_REQUEST);
        }
        Feedback feedback = feedbackService.createFeedback(content, order_id);
        if(feedback != null) {
            ApiResponseDTO<String> response = new ApiResponseDTO<>("Feedback created", "Feedback id: " + feedback.feedback_id);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ApiResponseDTO<String> response = new ApiResponseDTO<>("Invalid order id", null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
