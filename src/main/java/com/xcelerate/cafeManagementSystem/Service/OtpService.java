package com.xcelerate.cafeManagementSystem.Service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private CacheManager cacheManager;

    private static final int OTP_VALID_DURATION = 5; // OTP validity duration in minutes

    public boolean generateAndSendOtp(String email) {
        String otp = generateOtp();
        String subject = "Your OTP Code";
        String text = "Your OTP code is:  " + otp;

        // Store OTP in cache with expiration
        cacheManager.getCache("otpCache").put(email, new OtpDetails(otp, LocalDateTime.now().plusMinutes(OTP_VALID_DURATION)));

        // Send OTP email
        try {
            emailService.sendEmail(email, subject, text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generate a 6-digit OTP
        return String.valueOf(otp);
    }

    public boolean validateOtp(String email, String otp) {
        OtpDetails otpDetails = cacheManager.getCache("otpCache").get(email, OtpDetails.class);
        if (otpDetails != null && otpDetails.getOtp().equals(otp) && otpDetails.getExpirationTime().isAfter(LocalDateTime.now())) {
            cacheManager.getCache("otpCache").evict(email); // Remove OTP after successful validation
            return true;
        }
        return false;
    }

    private static class OtpDetails {
        private String otp;
        private LocalDateTime expirationTime;

        public OtpDetails(String otp, LocalDateTime expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public LocalDateTime getExpirationTime() {
            return expirationTime;
        }
    }
}