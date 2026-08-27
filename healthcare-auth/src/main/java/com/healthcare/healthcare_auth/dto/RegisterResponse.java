package com.healthcare.healthcare_auth.dto;

import com.healthcare.healthcare_auth.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

@Schema(description = ApiDescription.REGISTER_RESPONSE)
@Builder
public record RegisterResponse(

        String username,
        String email,
        Instant createdAt,
        String message,
        boolean emailVerificationRequired
) {}


