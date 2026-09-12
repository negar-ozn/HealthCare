package com.healthcare.healthcare_auth.dto;

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
