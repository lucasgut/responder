package com.temenos.responder.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by aburgos on 16/01/2017.
 */
public class MethodNotFoundException extends WebApplicationException {
    private String message;
    private Throwable cause;
    private Response response;

    public MethodNotFoundException() {
    }

    public MethodNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public MethodNotFoundException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    public MethodNotFoundException(Response response) {
        super(response);
        this.response = response;
    }

    public MethodNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    public MethodNotFoundException(String message, Response response) {
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
