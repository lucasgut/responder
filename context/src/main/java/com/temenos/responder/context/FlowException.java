package com.temenos.responder.context;

public class FlowException extends RuntimeException {
    private final int status;
    private final String message;
    private final String internalErrorCode;

    public FlowException(int status, String message, String internalErrorCode) {
        this.status = status;
        this.message = message;
        this.internalErrorCode = internalErrorCode;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public String getInternalErrorCode() {
        return internalErrorCode;
    }
}
