package com.ojih.rex.eventplanner.exception.user;

public class UserUnauthenticatedException extends UserException {
    public UserUnauthenticatedException(String message) {
        super(message);
    }

    public UserUnauthenticatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserUnauthenticatedException(Throwable cause) {
        super(cause);
    }
}
