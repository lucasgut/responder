package com.temenos.responder.entity.exception;

/**
 * Created by Douglas Groves on 18/12/2016.
 */
public class TypeMismatchException extends EntityException {
    private String message;
    private Throwable cause;

    public TypeMismatchException(){}

    public TypeMismatchException(String message){
        super(message);
        this.message = message;
    }

    public TypeMismatchException(Throwable cause){
        super(cause);
        this.cause = cause;
    }

    public TypeMismatchException(String message, Throwable cause){
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
