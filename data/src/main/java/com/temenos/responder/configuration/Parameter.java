package com.temenos.responder.configuration;

/**
 * Created by aburgos on 30/01/2017.
 */
public class Parameter {
    private final String name;
    private final String type;
    private String description;
    private final ParameterLocation in;

    public Parameter(String name, String type, ParameterLocation in) {
        this.name = name;
        this.type = type;
        this.in = in;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() { return name; }

    public String getType() { return type; }

    public String getDescription() { return description; }

    public ParameterLocation getIn() { return in; }
}
