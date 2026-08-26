package com.healthcare.healthcare_audit.exception;

import lombok.Builder;

@Builder
public record FieldValidationError(

        String field,
        String message
) {
}
