package com.ojih.rex.eventplanner.exception;

public class UserServiceException extends Exception{
    public UserServiceException(String message) {
        super(message);
    }

    public UserServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
