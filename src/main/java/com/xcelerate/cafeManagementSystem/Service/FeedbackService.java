package com.xcelerate.cafeManagementSystem.Service;

import com.xcelerate.cafeManagementSystem.Model.Feedback;
import com.xcelerate.cafeManagementSystem.Model.Order;
import com.xcelerate.cafeManagementSystem.Repository.FeedbackRepository;
import com.xcelerate.cafeManagementSystem.Repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FeedbackService {

    @Autowired
    private EmotionAnalysisService emotionAnalysisService;

    @Autowired
    private FeedbackRepository feedbackRepository;



    @Transactional(rollbackFor = Exception.class)
    public Feedback createFeedback(String content, Order order) {
        Feedback feedback = new Feedback();
        feedback.content = content;
        if(order == null) {
            return null;
        }
        feedback.order = order;
        feedback.emotion = emotionAnalysisService.getEmotion(content);
        return feedbackRepository.save(feedback);
    }

    public List<Feedback> getFeedbacks() {
        return feedbackRepository.findAll();
    }

}
