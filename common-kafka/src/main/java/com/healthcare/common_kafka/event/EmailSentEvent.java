package com.healthcare.common_kafka.event;

import lombok.Builder;

@Builder
public record EmailSentEvent(

        Long userId,
        String email,
        String username,
        String action,
        String entityName,
        String status,
        String details,
        String eventType
) {
}
