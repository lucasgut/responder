package com.temenos.responder.adapter;

import lombok.Builder;
import lombok.Data;

import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;

@Data
@Builder
public class AdapterContext<P extends AdapterParameters> {
    private P parameters;

    private MultivaluedMap<String, String> queryParameters;
    private Map<String, String> headers;
    private Map<String, Object> attributes;

    private String languagePreference;
    private String principal;
}
