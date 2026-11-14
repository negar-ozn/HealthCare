package com.healthcare.healthcare_api.shared.tokenIntrospectDto;

import lombok.*;

import java.time.Instant;
import java.util.Set;

@Builder
public record TokenIntrospectionResponse (
        boolean valid,
        Long userId,
        String username,
        String email,
        Set<String> authorities,
        Instant expiresAt
) {}
