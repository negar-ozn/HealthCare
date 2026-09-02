package com.healthcare.healthcare_notification.controller;

import com.healthcare.healthcare_notification.dto.VerificationEmailRequest;
import com.healthcare.healthcare_notification.service.NotificationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping("/verification-email")
    public void sendVerificationEmail(VerificationEmailRequest request) {

        notificationService.sendVerificationEmail(request);
    }
}
