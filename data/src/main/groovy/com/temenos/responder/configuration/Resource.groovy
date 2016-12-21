package com.temenos.responder.configuration

import com.temenos.responder.commands.Command
import com.temenos.responder.commands.Scaffold;

import javax.ws.rs.core.Response;
import java.util.Map;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class Resource {
    private final String pathSpec;
    private final String nameSpec;
    private final String httpMethod;
    private final Map<String,Class<Scaffold>> modelSpec;
    private final Command flowSpec;
    private final String scope;

    public Resource(String pathSpec, String nameSpec, String httpMethod, Map<String,Class<Scaffold>> modelSpec, Command flowSpec, String scope){
        this.pathSpec = pathSpec;
        this.nameSpec = nameSpec;
        this.httpMethod = httpMethod;
        this.modelSpec = modelSpec;
        this.flowSpec = flowSpec;
        this.scope = scope;
    }

    public String getPathSpec() {
        return pathSpec
    }

    public String getNameSpec() {
        return nameSpec
    }

    public Map<String, Class<Scaffold>> getModelSpec() {
        return modelSpec
    }

    public Command getFlowSpec() {
        return flowSpec
    }

    public String getScope() {
        return scope
    }

    public String getHttpMethod() {
        return httpMethod
    }
}
