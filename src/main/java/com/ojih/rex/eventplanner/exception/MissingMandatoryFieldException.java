package com.ojih.rex.eventplanner.exception;

public class MissingMandatoryFieldException extends RuntimeException {
    public MissingMandatoryFieldException(String message) {
        super(message);
    }

    public MissingMandatoryFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingMandatoryFieldException(Throwable cause) {
        super(cause);
    }
}
