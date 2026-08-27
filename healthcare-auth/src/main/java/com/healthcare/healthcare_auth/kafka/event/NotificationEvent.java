package com.healthcare.healthcare_auth.kafka.event;

import com.healthcare.healthcare_auth.enums.NotificationSource;
import com.healthcare.healthcare_auth.enums.NotificationType;
import lombok.Builder;

import java.util.Map;

@Builder
public record NotificationEvent(

        String correlationId,
        NotificationType type,
        String recipient,
        NotificationSource sender,
        String subject,
        Map<String, Object> variables
) {
}
