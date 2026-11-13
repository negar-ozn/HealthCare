package com.healthcare.healthcare_notification.kafka.consumer;

import com.healthcare.common_kafka.event.NotificationEvent;
import com.healthcare.healthcare_notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PersonnelPasswordSetupEmailConsumer {

    private final NotificationService notificationService;

    public PersonnelPasswordSetupEmailConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = {"personnel-password-setup-link"},
            groupId = "healthcare-notification-group",
            containerFactory = "notificationKafkaListenerContainerFactory")
    public void consume(NotificationEvent event) {

        log.info("Notification received. correlationId={}", event.correlationId());
        notificationService.processPersonnelPasswordSetupEmail(event);

    }
}
