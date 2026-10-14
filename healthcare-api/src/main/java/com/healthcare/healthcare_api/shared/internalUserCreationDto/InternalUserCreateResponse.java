package com.healthcare.healthcare_api.shared.internalUserCreationDto;

import lombok.Builder;

@Builder
public record InternalUserCreateResponse(

        Long userId,
        String username,
        String email,
        String role
) {
}
