package com.healthcare.healthcare_audit.kafka.event;

import lombok.Builder;

@Builder
public record UserRegisteredEvent(

        Long userId,
        String email,
        String username,
        String verificationLink,
        String action,
        String status,
        String detail,
        String entityName
) {
}
