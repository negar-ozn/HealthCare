package com.healthcare.healthcare_api.shared.exception;

public class AppointmentModificationIsUnavailableException extends RuntimeException {
    public AppointmentModificationIsUnavailableException(String message) {
        super(message);
    }
}
