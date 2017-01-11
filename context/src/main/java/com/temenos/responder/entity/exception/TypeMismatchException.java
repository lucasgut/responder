package com.temenos.responder.entity.exception;

/**
 * This exception should be thrown if there is a type mismatch between two
 * {@link com.temenos.responder.entity.runtime.Entity entities} or an
 * {@link com.temenos.responder.entity.runtime.Entity entity} and a {@link com.temenos.responder.scaffold.Scaffold scaffold}.
 *
 * @author Douglas Groves
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
