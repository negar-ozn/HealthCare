package com.healthcare.healthcare_auth.kafka.event;


import lombok.Builder;

@Builder
public record UserRegisteredEvent(

        Long userId,
        String email,
        String username,
        String verificationLink,
        String auction,
        String status,
        String detail,
        String entityName
) {
}
