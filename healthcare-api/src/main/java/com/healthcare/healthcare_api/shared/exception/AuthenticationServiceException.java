package com.healthcare.healthcare_api.shared.exception;

public class AuthenticationServiceException extends RuntimeException {
    public AuthenticationServiceException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
