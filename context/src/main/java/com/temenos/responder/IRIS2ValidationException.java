package com.temenos.responder;

import java.util.List;

public class IRIS2ValidationException extends IRIS2Exception {
    private final List<IRIS2ValidationError> validationErrors;

    public IRIS2ValidationException(IRIS2Error error, List<IRIS2ValidationError> validationErrors) {
        super(error);
        this.validationErrors = validationErrors;
    }

    public IRIS2ValidationException(IRIS2Error error, List<IRIS2ValidationError> validationErrors, Throwable cause) {
        super(error, cause);
        this.validationErrors = validationErrors;
    }
}
