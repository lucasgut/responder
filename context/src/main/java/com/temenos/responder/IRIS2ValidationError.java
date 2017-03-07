package com.temenos.responder;

import lombok.Builder;

import java.util.Map;

@Builder
public class IRIS2ValidationError {
    String field;
    String errorCode;
    Map<String, String> tags;
}
