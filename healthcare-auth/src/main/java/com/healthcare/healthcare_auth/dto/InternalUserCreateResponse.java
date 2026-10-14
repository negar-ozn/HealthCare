package com.healthcare.healthcare_auth.dto;

import com.healthcare.healthcare_auth.enums.UserRole;
import lombok.Builder;

@Builder
public record InternalUserCreateResponse(

        Long userId,
        String username,
        String email,
        UserRole role
) {
}
