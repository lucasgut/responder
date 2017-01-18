package com.temenos.responder.context;

import com.temenos.responder.dispatcher.Dispatcher;
import com.temenos.responder.entity.runtime.Entity;

/**
 * Created by dgroves on 18/01/2017.
 */
public class ExecutionContextBuilder {

    private String origin;
    private String resourceName;
    private Parameters requestParameters;
    private Entity requestBody;
    private Dispatcher dispatcher;

    public ExecutionContext build() {
        return new DefaultExecutionContext(origin, resourceName, requestParameters, requestBody, dispatcher);
    }

    public ExecutionContextBuilder origin(String origin) {
        this.origin = origin;
        return this;
    }

    public ExecutionContextBuilder resourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public ExecutionContextBuilder requestParameters(Parameters requestParameters) {
        this.requestParameters = requestParameters;
        return this;
    }

    public ExecutionContextBuilder requestBody(Entity requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public ExecutionContextBuilder dispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }
}
