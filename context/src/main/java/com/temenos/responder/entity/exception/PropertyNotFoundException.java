package com.temenos.responder.entity.exception;

/**
 * Created by Douglas Groves on 18/12/2016.
 */
public class PropertyNotFoundException extends EntityException {
    private String message;
    private Throwable cause;

    public PropertyNotFoundException(){}

    public PropertyNotFoundException(String message){
        super(message);
        this.message = message;
    }

    public PropertyNotFoundException(Throwable cause){
        super(cause);
        this.cause = cause;
    }

    public PropertyNotFoundException(String message, Throwable cause){
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
