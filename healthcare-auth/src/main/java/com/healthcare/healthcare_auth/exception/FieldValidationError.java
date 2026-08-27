package com.healthcare.healthcare_auth.exception;

import lombok.Builder;

@Builder
public record FieldValidationError(

        String field,
        String message
) {
}
