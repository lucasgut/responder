package com.temenos.responder.adapter;

import lombok.Builder;
import lombok.Data;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;

@Data
@Builder
public class AdapterContext {
    private Map<String, Object> parameters;

    private MultivaluedMap<String, String> queryParameters;
    private Map<String, String> headers;

    private String languagePreference;
    private String principal;
}
