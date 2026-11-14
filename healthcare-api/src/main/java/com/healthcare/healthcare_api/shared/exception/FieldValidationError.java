package com.healthcare.healthcare_api.shared.exception;

import lombok.Builder;

@Builder
public record FieldValidationError(

        String field,
        String message
) {
}
