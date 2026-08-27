package com.healthcare.healthcare_notification.kafka.event;

import com.healthcare.healthcare_notification.enums.NotificationSource;
import com.healthcare.healthcare_notification.enums.NotificationType;

import java.util.Map;

public record NotificationEvent(

        String correlationId,
        NotificationType type,
        String recipient,
        NotificationSource sender,
        String subject,
        Map<String, Object> variables
) {
}
