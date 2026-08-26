package com.healthcare.healthcare_audit.kafka.event;

import lombok.Builder;

@Builder
public record EmailSentEvent(

        Long userId,
        String email,
        String username,
        String action,
        String entityName,
        String status,
        String details
) {
}
