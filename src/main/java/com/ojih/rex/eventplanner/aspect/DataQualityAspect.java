package com.ojih.rex.eventplanner.aspect;

import com.ojih.rex.eventplanner.annotation.DataQuality;
import com.ojih.rex.eventplanner.exception.MissingMandatoryFieldException;
import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.ojih.rex.eventplanner.constant.EventPlannerConstants.*;

@Aspect
@Component
public class DataQualityAspect {

    private static final Consumer<Object> DO_NOTHING = v -> {};

    @Before("execution(* *(..)) && @annotation(dataQuality)")
    public void checkDataQuality(JoinPoint joinPoint, DataQuality dataQuality) throws MissingMandatoryFieldException {
        EventPlannerRequest request = (EventPlannerRequest) joinPoint.getArgs()[1];
        List<String> missingMandatoryFields = new ArrayList<>();
        switch (dataQuality.requestType()) {
            case CREATE_USER:
                Optional.ofNullable(request.getUsername())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("username"));
                Optional.ofNullable(request.getFirstName())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("firstName"));
                Optional.ofNullable(request.getLastName())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("lastName"));
                Optional.ofNullable(request.getEmail())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("email"));
                Optional.ofNullable(request.getPassword())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("password"));
                Optional.ofNullable(request.getLocation())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("location"));
                break;
            case AUTHENTICATE_USER:
                Optional.ofNullable(request.getUsername())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("username"));
                Optional.ofNullable(request.getPassword())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("password"));
                break;
            case UPDATE_USER:
                Optional.ofNullable(request.getUserId())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("userId"));
                break;
            case GET_EVENTS_FROM_IDS:
                Optional.ofNullable(request.getEventIds())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("eventIds"));
                break;
            case SEARCH_EVENTS_BY_TITLE:
                Optional.ofNullable(request.getTitle())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("title"));
                break;
            case CREATE_EVENT:
                Optional.ofNullable(request.getHostId())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("hostId"));
                Optional.ofNullable(request.getTitle())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("title"));
                Optional.ofNullable(request.getDate())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("date"));
                Optional.ofNullable(request.getLocation())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("location"));
                break;
            case UPDATE_EVENT:
                Optional.ofNullable(request.getEventId())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("eventId"));
                break;
            case ADD_EVENT_ATTENDEE:
                Optional.ofNullable(request.getUserId())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("userId"));
                Optional.ofNullable(request.getEventId())
                        .ifPresentOrElse(DO_NOTHING, () -> missingMandatoryFields.add("eventId"));
                break;
            default:
                break;
        }
        if (!missingMandatoryFields.isEmpty())
            throw new MissingMandatoryFieldException("Missing mandatory fields: ["
                    + String.join(", ", missingMandatoryFields)
                    + "] for request type " + dataQuality.requestType());
    }
}
