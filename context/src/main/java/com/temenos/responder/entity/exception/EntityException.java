package com.temenos.responder.entity.exception;

/**
 * Subclasses of this {@link java.lang.RuntimeException exception} should be thrown if a problem occurs when
 * handling an {@link com.temenos.responder.entity.runtime.Entity entity object}.
 *
 * @author Douglas Groves
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
