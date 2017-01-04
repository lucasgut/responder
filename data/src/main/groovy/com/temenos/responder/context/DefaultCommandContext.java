package com.temenos.responder.context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Douglas Groves on 04/01/2017.
 */
public class DefaultCommandContext implements CommandContext {

    private final Map<String, Object> attributes;
    private String responseCode;

    public DefaultCommandContext(){
        this.attributes = new HashMap<>();
    }

    public DefaultCommandContext(Map<String, Object> attributes){
        this.attributes = attributes;
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean setAttribute(String name, Object value) {
        attributes.put(name, value);
        return true;
    }

    @Override
    public String getResponseCode() {
        return responseCode;
    }

    @Override
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
