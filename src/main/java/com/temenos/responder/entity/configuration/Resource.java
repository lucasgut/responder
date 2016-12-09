package com.temenos.responder.entity.configuration;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
public class Resource {
    private final String pathSpec;
    private final String nameSpec;
    private final Map<Response.Status,Model> modelSpec;
    private final String scope;

    public Resource(String pathSpec, String nameSpec, Map<Response.Status,Model> modelSpec, String scope){
        this.pathSpec = pathSpec;
        this.nameSpec = nameSpec;
        this.modelSpec = modelSpec;
        this.scope = scope;
    }

    public String getPathSpec() {
        return pathSpec;
    }

    public String getNameSpec() {
        return nameSpec;
    }

    public Map<Response.Status,Model> getModelSpec() {
        return modelSpec;
    }

    public String getScope() {
        return scope;
    }
}
