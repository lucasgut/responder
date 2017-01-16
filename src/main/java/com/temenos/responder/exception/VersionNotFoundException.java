package com.temenos.responder.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

/**
 * Created by aburgos on 16/01/2017.
 */
public class VersionNotFoundException extends WebApplicationException {
    private String message;
    private Throwable cause;
    private Response response;

    public VersionNotFoundException() {
    }

    public VersionNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public VersionNotFoundException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    public VersionNotFoundException(Response response) {
        super(response);
        this.response = response;
    }

    public VersionNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    public VersionNotFoundException(String message, Response response) {
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
