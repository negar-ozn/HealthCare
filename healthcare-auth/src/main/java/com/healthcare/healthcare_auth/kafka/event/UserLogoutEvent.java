package com.healthcare.healthcare_auth.kafka.event;

import lombok.Builder;

@Builder
public record UserLogoutEvent(

        Long userId,
        String username,
        String action,
        String status,
        String detail
) {
}
