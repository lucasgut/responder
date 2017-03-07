package com.temenos.responder.flows.dashboard;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Builder
@Getter
public class Response {
    // Status
    private final int statusCode;

    // Payload
    private final byte[] body;

    // Headers
    private final Map<String, List<String>> headers;

    // Arguments
    private final Map<String, Object> arguments;
}
