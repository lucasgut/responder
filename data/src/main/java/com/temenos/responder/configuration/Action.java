package com.temenos.responder.configuration;

/**
 * Created by aburgos on 11/01/2017.
 */
public class Action {
    private final String description;
    private final Multiplicity multiplicity;
    private final String model;

    public static final String DESCRIPTION = "description";

    public Action(String description, Multiplicity multiplicity, String model) {
        this.description = description;
        this.multiplicity = multiplicity;
        this.model = model;
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    public String getModel() {
        return model;
    }

    public String getDescription() {
        return description;
    }
}