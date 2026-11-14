package com.healthcare.healthcare_auth.dto;

import com.healthcare.healthcare_auth.enums.UserRole;
import lombok.Builder;

@Builder
public record InternalUserCreateRequest(

        String username,
        String email,
        UserRole role
) {
}
