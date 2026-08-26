package com.healthcare.healthcare_api.shared.exception;

public class ScheduleAlreadyExistsException extends RuntimeException {
    public ScheduleAlreadyExistsException(String message) {
        super(message);
    }
}
