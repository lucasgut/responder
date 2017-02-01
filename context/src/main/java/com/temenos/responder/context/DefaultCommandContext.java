package com.temenos.responder.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default implementation of {@link com.temenos.responder.context.CommandContext CommandContext}.
 *
 * @author Douglas Groves
 */
public class DefaultCommandContext implements CommandContext {

    private final Map<String, Object> attributes;
    private List<String> fromParams;
    private String intoParam;
    private int responseCode;

    public DefaultCommandContext(){
        this.attributes = new HashMap<>();
        this.fromParams = new ArrayList<String>();
    }

    public DefaultCommandContext(Map<String, Object> attributes){
        this.attributes = attributes;
    }

    public DefaultCommandContext(List<String> fromParams, String intoParam) {
        this.attributes = new HashMap<>();
        this.fromParams = fromParams;
        this.intoParam = intoParam;
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
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public List<String> from() {
        return fromParams;
    }

    @Override
    public void from(List<String> params){
        this.fromParams = params;
    }

    @Override
    public String into() {
        return intoParam;
    }

    @Override
    public void into(String param){
        intoParam = param;
    }
}
