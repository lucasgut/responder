package com.temenos.responder.context;

import com.temenos.responder.configuration.Resource;
import com.temenos.responder.entity.runtime.Entity;

import java.util.Collections;
import java.util.Map;

/**
 * Created by dgroves on 18/01/2017.
 */
public class DefaultRequestContext implements RequestContext {

    private final String serverRoot;
    private final Resource resource;
    private final Parameters parameters;
    private final Entity requestBody;
    private final String origin;

    public DefaultRequestContext(String serverRoot, Resource resource, Parameters parameters, Entity requestBody, String origin){
        this.resource = resource;
        this.parameters = parameters;
        this.requestBody = requestBody;
        this.origin = origin;
        this.serverRoot = serverRoot;
    }

    @Override
    public String getResourcePath() {
        return resource.getPath();
    }

    @Override
    public String getResourceName() {
        return resource.getName();
    }

    @Override
    public Parameters getRequestParameters() {
        return parameters;
    }

    @Override
    public Entity getRequestBody() {
        return requestBody;
    }

    @Override
    public Object getAttribute(String name) {
        return parameters.getValue(name);
    }

    @Override
    public boolean setAttribute(String name, Object value) {
        parameters.setValue(name, (String)value);
        return true;
    }

    @Override
    public String getOrigin() {
        return origin;
    }

    @Override
    public String getServerRoot() {
        return this.serverRoot;
    }
}
