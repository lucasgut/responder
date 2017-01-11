package com.temenos.responder.configuration;

/**
 * Created by aburgos on 11/01/2017.
 */
public enum Status {
    ACTIVE("active"),
    DEPRECATED("deprecated"),
    DELETED("deleted");

    private final String value;

    Status(final String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
