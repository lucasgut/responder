package com.temenos.responder.configuration;

/**
 * Created by Douglas Groves on 21/12/2016.
 */
public enum HttpMethods {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE"),
    HEAD("HEAD");

    private final String value;

    HttpMethods(final String value){
        this.value = value;
    }

    public String getValue(){
       return value;
    }
}