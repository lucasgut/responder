package com.temenos.responder.configuration;

/**
 * An HttpMethod is an enumeration of an HTTP method.
 *
 * @author Douglas Groves
 */
public enum HttpMethods {
    /**
     * HTTP GET method.
     */
    GET("GET"),
    /**
     * HTTP POST method.
     */
    POST("POST"),
    /**
     * HTTP PUT method.
     */
    PUT("PUT"),
    /**
     * HTTP DELETE method.
     */
    DELETE("DELETE"),
    /**
     * HTTP HEAD method.
     */
    HEAD("HEAD");

    private final String value;

    HttpMethods(final String value){
        this.value = value;
    }

    /**
     * The string value of a given enumeration.
     *
     * @return A string representation of the given enumeration.
     */
    public String getValue(){
       return value;
    }
}