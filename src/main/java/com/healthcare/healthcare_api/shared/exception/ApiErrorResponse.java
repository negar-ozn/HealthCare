package com.healthcare.healthcare_api.shared.exception;

import com.healthcare.healthcare_api.shared.enums.ErrorCode;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ApiErrorResponse (

    LocalDateTime timestamp,
    int status,
    String error,
    ErrorCode code,
    String message,
    String path,
    List<FieldValidationError> validationErrors){
}
