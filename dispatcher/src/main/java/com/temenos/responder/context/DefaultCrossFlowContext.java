package com.temenos.responder.context;

import com.temenos.responder.configuration.Origin;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Douglas Groves
 */
public class DefaultCrossFlowContext implements CrossFlowContext {

    private final Origin origin;
    private final List<Parameters> parameters;
    private final List<String> into;
    private final Map<String, Object> attributes;

    public DefaultCrossFlowContext(Origin origin, List<Parameters> parameters, List<String> into){
        this(origin, parameters, into, new ConcurrentHashMap<>());
    }

    DefaultCrossFlowContext(Origin origin, List<Parameters> parameters, List<String> into, Map<String,Object> attributes){
        this.parameters = parameters;
        this.into = into;
        this.origin = origin;
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
    public Origin getOrigin() {
        return origin;
    }

    @Override
    public List<Parameters> allParameters() {
        return parameters;
    }

    @Override
    public Parameters parameters() {
        return parameters.get(0);
    }

    @Override
    public List<String> into() {
        return into;
    }
}
