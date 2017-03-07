package com.temenos.responder;

import lombok.Builder;

import java.util.Map;

@Builder
public class IRIS2Error {
    IRIS2ErrorType type;
    String errorCode;
    Map<String, String> tags;
}
