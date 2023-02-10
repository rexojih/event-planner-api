package com.ojih.rex.eventplanner.controller;

import com.ojih.rex.eventplanner.exception.MissingMandatoryFieldException;
import com.ojih.rex.eventplanner.exception.ProcessorNotFoundException;
import com.ojih.rex.eventplanner.exception.event.EventNotFoundException;
import com.ojih.rex.eventplanner.exception.user.ExistingUserException;
import com.ojih.rex.eventplanner.exception.user.UserNotFoundException;
import com.ojih.rex.eventplanner.exception.user.UserUnauthenticatedException;
import com.ojih.rex.eventplanner.model.response.EventPlannerErrorMessage;
import com.ojih.rex.eventplanner.model.response.EventPlannerErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(
            ExistingUserException.class
    )
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public EventPlannerErrorResponse handleUserServiceException(Exception e) {
        return EventPlannerErrorResponse.builder()
                .eventPlannerErrorMessage(EventPlannerErrorMessage.builder()
                        .type(HttpStatus.CONFLICT.getReasonPhrase())
                        .status(String.valueOf(HttpStatus.CONFLICT.value()))
                        .exception(e.getClass().getSimpleName())
                        .message(e.getMessage())
                        .build())
                .build();
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            EventNotFoundException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public EventPlannerErrorResponse handleNotFoundExceptionException(Exception e) {
        return EventPlannerErrorResponse.builder()
                .eventPlannerErrorMessage(EventPlannerErrorMessage.builder()
                        .type(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .status(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .exception(e.getClass().getSimpleName())
                        .message(e.getMessage())
                        .build())
                .build();
    }

    @ExceptionHandler(
            UserUnauthenticatedException.class
    )
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public EventPlannerErrorResponse handleUserUnauthenticatedException(Exception e) {
        return EventPlannerErrorResponse.builder()
                .eventPlannerErrorMessage(EventPlannerErrorMessage.builder()
                        .type(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                        .status(String.valueOf(HttpStatus.UNAUTHORIZED.value()))
                        .exception(e.getClass().getSimpleName())
                        .message(e.getMessage())
                        .build())
                .build();
    }

    @ExceptionHandler({
            ParseException.class,
            MissingMandatoryFieldException.class,
            IllegalArgumentException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public EventPlannerErrorResponse handleInputExceptions(Exception e) {
        return EventPlannerErrorResponse.builder()
                .eventPlannerErrorMessage(EventPlannerErrorMessage.builder()
                        .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .status(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                        .exception(e.getClass().getSimpleName())
                        .message(e.getMessage())
                        .build())
                .build();
    }

    @ExceptionHandler({
            ProcessorNotFoundException.class
    })
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public EventPlannerErrorResponse handleServerExceptions(Exception e) {
        return EventPlannerErrorResponse.builder()
                .eventPlannerErrorMessage(EventPlannerErrorMessage.builder()
                        .type(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                        .status(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                        .exception(e.getClass().getSimpleName())
                        .message(e.getMessage())
                        .build())
                .build();
    }
}
