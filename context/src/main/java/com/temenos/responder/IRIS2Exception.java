package com.temenos.responder;

public class IRIS2Exception extends RuntimeException {
    private final IRIS2Error error;

    public IRIS2Exception(IRIS2Error error) {
        this.error = error;
    }

    public IRIS2Exception(IRIS2Error error, Throwable cause) {
        super(cause);
        this.error = error;
    }
}
