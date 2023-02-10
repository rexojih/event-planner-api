package com.ojih.rex.eventplanner.handler;

import com.ojih.rex.eventplanner.exception.ProcessorNotFoundException;
import com.ojih.rex.eventplanner.model.request.EventPlannerRequest;
import com.ojih.rex.eventplanner.model.response.EventPlannerResponse;
import com.ojih.rex.eventplanner.processor.RequestProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RequestHandler {

    private final Map<String, RequestProcessor> requestProcessorRegistry;

    public EventPlannerResponse handleRequest(EventPlannerRequest request, String requestType) throws ParseException {
        RequestProcessor processor = requestProcessorRegistry.get(requestType + "Processor");
        if (processor == null)
            throw new ProcessorNotFoundException("Unable to process request type: " + requestType);
        return processor.process(request);
    }
}
