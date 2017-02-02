package com.temenos.responder.context;

public class FlowException extends RuntimeException {
    private final int status;
    private final String message;

    public FlowException(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
