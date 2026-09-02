package com.healthcare.common_kafka.event;

import com.healthcare.common_kafka.enums.NotificationSource;
import com.healthcare.common_kafka.enums.NotificationType;
import lombok.Builder;

import java.util.Map;

@Builder
public record NotificationEvent(

        String correlationId,
        NotificationType type,
        String recipient,
        NotificationSource sender,
        String subject,
        Map<String, Object> variables,
        String eventType
) {
}
