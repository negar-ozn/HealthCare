package com.healthcare.healthcare_notification.kafka.event;

import lombok.Builder;

@Builder
public record EmailSentEvent(

        Long userId,
        String email,
        String username,
        String auditAction,
        String entityName,
        String auditStatus,
        String details
) {
}
