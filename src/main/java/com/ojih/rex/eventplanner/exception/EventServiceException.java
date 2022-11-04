package com.ojih.rex.eventplanner.exception;

public class EventServiceException extends Exception {

    public EventServiceException(String message) {
        super(message);
    }

    public EventServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
