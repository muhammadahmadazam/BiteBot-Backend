package com.xcelerate.cafeManagementSystem.Service;

import java.util.Base64;
import java.util.concurrent.CompletableFuture;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.xcelerate.cafeManagementSystem.Utils.GmailAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

@Service
public class EmailService {

    private static final String TEST_EMAIL = "xcelerate.bitebot@gmail.com";



    public void sendEmail(String to, String subject, String bodyText) throws Exception {
        GmailAuthentication gmailAuthentication = new GmailAuthentication();
        Gmail service = gmailAuthentication.getService();

        // Create raw email content
        String rawEmail = "To: " + to + "\r\n"
                + "Subject: " + subject + "\r\n"
                + "\r\n"
                + bodyText;

        // Encode the email to Base64 URL-safe format
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawEmail.getBytes());

        // Create Gmail API Message
        Message message = new Message();
        message.setRaw(encodedEmail);

        // Send the email
        service.users().messages().send("me", message).execute();

        System.out.println("Email sent successfully to " + to);
    }
}