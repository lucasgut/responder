package com.temenos.responder.entity.exception;

/**
 * This execption should be thrown if the given field name does not exist.
 *
 * @author Douglas Groves
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
