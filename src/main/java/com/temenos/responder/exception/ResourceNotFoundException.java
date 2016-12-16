package com.temenos.responder.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by Douglas Groves on 12/12/2016.
 */
public class ResourceNotFoundException extends WebApplicationException {
    private String message;
    private Throwable cause;
    private Response response;

    public ResourceNotFoundException() {
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public ResourceNotFoundException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    public ResourceNotFoundException(Response response) {
        super(response);
        this.response = response;
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    public ResourceNotFoundException(String message, Response response) {
        super(message, response);
        this.message = message;
        this.response = response;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public Response getResponse() {
        return response;
    }
}
