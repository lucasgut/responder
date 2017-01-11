package com.temenos.responder.exception;

import groovy.lang.Script;

/**
 * Thrown if an error occurs while executing a JSR-223 script.
 *
 * @author Douglas Groves
 */
class ScriptExecutionException extends Exception {
    private String message;
    private Throwable cause;

    public ScriptExecutionException(){}

    public ScriptExecutionException(String message){
        super(message);
        this.message = message;
    }

    public ScriptExecutionException(Throwable cause){
        super(cause);
        this.cause = cause;
    }

    public ScriptExecutionException(String message, Throwable cause){
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
