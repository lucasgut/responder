package com.temenos.responder.configuration;

import com.temenos.responder.commands.Command;
import com.temenos.responder.flows.Flow;
import com.temenos.responder.scaffold.Scaffold;

import java.util.Map;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
public class Resource {
    private final String pathSpec;
    private final String nameSpec;
    private final String httpMethod;
    private final Class<Scaffold> inputModelSpec;
    private final Map<String,Class<Scaffold>> outputModelSpec;
    private final Flow flowSpec;
    private final String scope;

    public Resource(String pathSpec, String nameSpec, String httpMethod, Class<Scaffold> inputModelSpec,
                    Map<String,Class<Scaffold>> outputModelSpec, Flow flowSpec, String scope){
        this.pathSpec = pathSpec;
        this.nameSpec = nameSpec;
        this.httpMethod = httpMethod;
        this.inputModelSpec = inputModelSpec;
        this.outputModelSpec = outputModelSpec;
        this.flowSpec = flowSpec;
        this.scope = scope;
    }

    public String getPathSpec() {
        return pathSpec;
    }

    public String getNameSpec() {
        return nameSpec;
    }

    public Class<Scaffold> getInputModelSpec(){
        return inputModelSpec;
    }

    public Map<String, Class<Scaffold>> getOutputModelSpec() {
        return outputModelSpec;
    }

    public Flow getFlowSpec() {
        return flowSpec;
    }

    public String getScope() {
        return scope;
    }

    public String getHttpMethod() {
        return httpMethod;
    }
}
