package com.temenos.responder.configuration;

/**
 * Created by aburgos on 30/01/2017.
 */
public enum ParameterLocation {
    header("header"),
    query("query"),
    path("path");

    private final String value;

    ParameterLocation(final String value){
            this.value = value;
        }

    public String getValue(){
            return value;
        }
}
