package com.healthcare.healthcare_auth.dto;

import lombok.Builder;

@Builder
public record UserInfoResponse(

        Long userId,
        String username,
        String email
) {
}
