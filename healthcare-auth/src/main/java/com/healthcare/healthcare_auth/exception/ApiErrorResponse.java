package com.healthcare.healthcare_auth.exception;

import com.healthcare.healthcare_auth.enums.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


@Schema(description = "Standard API error response")
@Builder
public record ApiErrorResponse(

        @Schema(example = "2026-07-30T20:30:00Z")
        LocalDateTime timestamp,
        @Schema(example = "401")
        int status,
        @Schema(example = "Access denied")
        String error,
        @Schema(example = "INVALID_CREDENTIALS")
        ErrorCode code,
        @Schema(example = "Username or password is incorrect.")
        String message,
        @Schema(example = "/api/v1/auth/login")
        String path,
        @Schema(example = "???")
        List<FieldValidationError> validationErrors
) {
}
