package com.healthcare.healthcare_audit.exception;

import com.healthcare.healthcare_audit.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchAuditLogFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAuditLogNotFound(NoSuchAuditLogFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),ErrorCode.AUDIT_LOG_NOT_FOUND,
                ex.getMessage(), request, null);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            int status, String error, ErrorCode code, String message,
            HttpServletRequest request, List<FieldValidationError> validationErrors) {
        ApiErrorResponse errorResponse = ApiErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status)
                .error(error)
                .code(code)
                .message(message)
                .path(request.getRequestURI())
                .validationErrors(validationErrors)
                .build();

        return ResponseEntity.status(status).body(errorResponse);
    }
}
