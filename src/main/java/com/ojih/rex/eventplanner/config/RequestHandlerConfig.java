package com.ojih.rex.eventplanner.config;

import com.ojih.rex.eventplanner.processor.RequestProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class RequestHandlerConfig {

    @Bean("requestProcessorRegistry")
    public Map<String, RequestProcessor> registerRequestProcessors(List<RequestProcessor> requestHandlers) {
        return requestHandlers.stream().collect(Collectors.toMap(RequestProcessor::processorType, Function.identity()));
    }
}
