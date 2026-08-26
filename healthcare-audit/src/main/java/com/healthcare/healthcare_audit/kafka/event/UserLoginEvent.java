package com.healthcare.healthcare_audit.kafka.event;

import lombok.Builder;

@Builder
public record UserLoginEvent(

        Long userId,
        String username,
        String action,
        String entityName,
        String status,
        String detail
) {
}
