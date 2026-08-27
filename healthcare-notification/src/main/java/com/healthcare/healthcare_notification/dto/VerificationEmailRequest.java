package com.healthcare.healthcare_notification.dto;

import lombok.Builder;

@Builder
public record VerificationEmailRequest(

        Long userId,
        String email,
        String username,
        String verificationLink
) {
}
