package com.healthcare.healthcare_auth.dto;

import com.healthcare.healthcare_auth.util.openApiDoc.ApiDescription;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.Set;

@Schema(description = ApiDescription.INTROSPECT_RESPONSE)
@Builder
public record TokenIntrospectionResponse(

        boolean valid,
        Long userId,
        String username,
        String email,
        Set<String> authorities,
        Instant expiresAt

) {
}

