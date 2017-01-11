package com.temenos.responder.configuration;

/**
 * Created by aburgos on 11/01/2017.
 */
public enum Multiplicity {
    ITEM("item"),
    COLLECTION("collection");

    private final String value;

    Multiplicity(final String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
