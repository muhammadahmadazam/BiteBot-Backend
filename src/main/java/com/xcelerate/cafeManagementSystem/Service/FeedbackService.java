package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.Model.Feedback;
import com.xcelerate.cafeManagementSystem.Model.Order;
import com.xcelerate.cafeManagementSystem.Repository.FeedbackRepository;
import com.xcelerate.cafeManagementSystem.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

    @Autowired
    private EmotionAnalysisService emotionAnalysisService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Feedback createFeedback(String content, String order_id) {
        Feedback feedback = new Feedback();
        feedback.content = content;
        Order order = orderRepository.findByOrderId(order_id);
        if(order == null) {
            return null;
        }
        feedback.order = order;
        feedback.emotion = emotionAnalysisService.getEmotion(content);
        return feedbackRepository.save(feedback);
    }

}
