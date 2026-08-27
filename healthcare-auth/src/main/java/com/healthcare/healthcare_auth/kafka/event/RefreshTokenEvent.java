package com.healthcare.healthcare_auth.kafka.event;

import lombok.Builder;

@Builder
public record RefreshTokenEvent(

        Long userId,
        String username,
        String action,
        String entityName,
        String status,
        String detail
) {
}
