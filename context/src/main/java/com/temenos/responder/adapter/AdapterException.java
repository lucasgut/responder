package com.temenos.responder.adapter;

import com.temenos.responder.IRIS2Exception;

public class AdapterException extends IRIS2Exception {
    private final String code;
    private final AdapterFailureType type;

    public AdapterException(String code, String message, AdapterFailureType type) {
        super(message);
        this.code = code;
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public AdapterFailureType getFailureType() {
        return type;
    }
}
