package com.healthcare.healthcare_api.shared.exception;

public class NoSuchInvoiceFoundException extends RuntimeException {
    public NoSuchInvoiceFoundException(String message) {
        super(message);
    }
}
