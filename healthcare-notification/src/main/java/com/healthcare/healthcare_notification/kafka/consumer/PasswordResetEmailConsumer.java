package com.healthcare.healthcare_notification.kafka.consumer;

import com.healthcare.common_kafka.event.NotificationEvent;
import com.healthcare.healthcare_notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetEmailConsumer {

    private final NotificationService notificationService;

    public PasswordResetEmailConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = "password-reset",
            groupId = "healthcare-notification-group",
            containerFactory = "notificationKafkaListenerContainerFactory")
    public void consume(NotificationEvent event) {

        notificationService.processForgetPasswordEmailNotification(event);
    }
}
