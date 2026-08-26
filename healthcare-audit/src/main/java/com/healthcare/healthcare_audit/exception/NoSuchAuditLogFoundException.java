package com.healthcare.healthcare_audit.exception;

public class NoSuchAuditLogFoundException extends RuntimeException {
    public NoSuchAuditLogFoundException(String message) {
        super(message);
    }
}
