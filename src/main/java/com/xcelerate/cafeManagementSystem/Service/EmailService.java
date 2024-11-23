package com.xcelerate.cafeManagementSystem.Service;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;

import java.util.Properties;


@Service
public class EmailService {

    @Autowired
    GmailAuthenticationService gmailAuth;

    public void sendEmail(String to, String subject, String body) {
        try {
            Gmail service = gmailAuth.getService();
            MimeMessage email = createEmail(to, "xcelerate.bitebot@gmail.com", subject, body);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            email.writeTo(buffer);
            Message message = new Message();
            message.setRaw(Base64.encodeBase64URLSafeString(buffer.toByteArray()));
            service.users().messages().send("me", message).execute();
            System.out.println("Email sent successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private MimeMessage createEmail(String to, String from, String subject, String body) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress(from));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject(subject);
        email.setText(body);

        return email;
    }


}
