package com.ojih.rex.eventplanner.exception.user;

public class ExistingUserException extends UserException {
    public ExistingUserException(String message) {
        super(message);
    }

    public ExistingUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistingUserException(Throwable cause) {
        super(cause);
    }
}
