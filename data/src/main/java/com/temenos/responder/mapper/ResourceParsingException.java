package com.temenos.responder.mapper;

/**
 * Created by aburgos on 11/01/2017.
 */
public class ResourceParsingException extends RuntimeException {

    private String message;
    private Throwable cause;

    public ResourceParsingException() {}

    public ResourceParsingException(String message) {
        super(message);
        this.message = message;
    }

    public ResourceParsingException(Throwable cause) {
        super(cause);
        this.cause = cause;
    }

    public ResourceParsingException(String message, Throwable cause) {
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