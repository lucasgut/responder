package com.temenos.responder.flows.dashboard;

import com.temenos.responder.context.FlowResult;

public class FlowResultFailure extends FlowResult {
    public FlowResultFailure(int status, String errorMessage) {
        super(null, null, status, errorMessage);
    }
}
