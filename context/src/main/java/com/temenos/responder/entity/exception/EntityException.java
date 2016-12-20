package com.temenos.responder.entity.exception;

/**
 * Created by Douglas Groves on 18/12/2016.
 */
public class EntityException extends RuntimeException {
    private String message;
    private Throwable cause;

    public EntityException(){}

    public EntityException(String message){
        super(message);
        this.message = message;
    }

    public EntityException(Throwable cause){
        super(cause);
        this.cause = cause;
    }

    public EntityException(String message, Throwable cause){
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
