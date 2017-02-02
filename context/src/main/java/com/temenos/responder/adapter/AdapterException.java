package com.temenos.responder.adapter;

public class AdapterException extends RuntimeException {
    private final int code;

    public AdapterException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
