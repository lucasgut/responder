package com.temenos.responder.configuration;

/**
 * Created by aburgos on 11/01/2017.
 */
public class Action {
    private String description;
    private final Multiplicity multiplicity;
    private final String model;

    public Action(Multiplicity multiplicity, String model) {
        this.multiplicity = multiplicity;
        this.model = model;
    }

    public Multiplicity getMultiplicity() {
        return multiplicity;
    }

    public String getModel() {
        return model;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}