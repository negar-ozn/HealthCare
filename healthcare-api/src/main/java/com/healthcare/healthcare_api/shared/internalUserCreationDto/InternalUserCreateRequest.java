package com.healthcare.healthcare_api.shared.internalUserCreationDto;

import lombok.Builder;

@Builder
public record InternalUserCreateRequest(

        String username,
        String email,
        String role
) {
}
