package com.healthcare.healthcare_notification.dto;

import lombok.Builder;
import lombok.NonNull;

@Builder
public record VerificationEmailRequest(

        Long userId,
        @NonNull
        String email,
        String username,
        String verificationLink
) {
}
