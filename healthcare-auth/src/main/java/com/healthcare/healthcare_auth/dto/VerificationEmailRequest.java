package com.healthcare.healthcare_auth.dto;

import lombok.Builder;

@Builder
public record VerificationEmailRequest(

        Long userId,
        String email,
        String username,
        String verificationLink
) {
}
