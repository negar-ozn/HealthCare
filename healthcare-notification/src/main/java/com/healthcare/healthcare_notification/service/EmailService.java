package com.healthcare.healthcare_notification.service;

import com.healthcare.healthcare_notification.entity.Notification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(Notification notification) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(
                        mimeMessage,
                        true,
                        StandardCharsets.UTF_8.name());
        helper.setTo(notification.getRecipient());
        helper.setSubject(notification.getSubject());

        helper.setText(notification.getBody(), true); // Means HTML, instead of plain text
        mailSender.send(mimeMessage);
    }

    public void sendEmail(String recipient, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
