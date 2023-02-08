package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.exception.MissingMandatoryFieldException;
import com.ojih.rex.eventplanner.exception.event.EventNotFoundException;
import com.ojih.rex.eventplanner.exception.user.ExistingUserException;
import com.ojih.rex.eventplanner.exception.user.UserNotFoundException;
import com.ojih.rex.eventplanner.exception.user.UserUnauthenticatedException;
import com.ojih.rex.eventplanner.model.response.EventPlannerErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(
            ExistingUserException.class
    )
    public ResponseEntity<EventPlannerErrorResponse> handleUserServiceException(Exception e) {
        return new ResponseEntity<>(EventPlannerErrorResponse.builder()
                .type(HttpStatus.CONFLICT.getReasonPhrase())
                .status(String.valueOf(HttpStatus.CONFLICT.value()))
                .exception(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            EventNotFoundException.class
    })
    public ResponseEntity<EventPlannerErrorResponse> handleNotFoundExceptionException(Exception e) {
        return new ResponseEntity<>(EventPlannerErrorResponse.builder()
                .type(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                .exception(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(
            UserUnauthenticatedException.class
    )
    public ResponseEntity<EventPlannerErrorResponse> handleUserUnauthenticatedException(Exception e) {
        return new ResponseEntity<>(EventPlannerErrorResponse.builder()
                .type(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .status(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .exception(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({
            ParseException.class,
            MissingMandatoryFieldException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<EventPlannerErrorResponse> handleInputExceptions(Exception e) {
        return new ResponseEntity<>(EventPlannerErrorResponse.builder()
                .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .exception(e.getClass().getSimpleName())
                .message(e.getMessage())
                .build(), HttpStatus.BAD_REQUEST);
    }
}
