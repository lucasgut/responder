package com.temenos.responder.exception;

/**
 * Created by Douglas Groves on 12/12/2016.
 */
public class ResourceNotFoundException extends Exception {
    private String message;
    private Throwable cause;

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

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
