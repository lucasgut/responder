package com.temenos.responder.flows.dashboard;

import com.temenos.responder.configuration.HttpMethod;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class Request {
    // Path info
    private final String uri;
    private final HttpMethod method;

    // Payload
    private final byte[] body;

    // Query parameters
    private final Map<String, List<String>> queryParameters;

    // Headers
    private final Map<String, List<String>> headers;

    // Arguments
    private final Map<String, Object> arguments;
}
