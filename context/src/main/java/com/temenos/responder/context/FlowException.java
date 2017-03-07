package com.temenos.responder.context;

import com.temenos.responder.IRIS2Exception;

public class FlowException extends IRIS2Exception {
     private final String internalErrorCode;

    public FlowException(String code, String message, String internalErrorCode) {
        super(message);
        this.code = code;
        this.internalErrorCode = internalErrorCode;
    }

    public String getCode() {
        return code;
    }

    public String getInternalErrorCode() {
        return internalErrorCode;
    }
}
