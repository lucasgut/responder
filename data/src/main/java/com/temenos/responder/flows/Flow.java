package com.temenos.responder.flows;

import com.temenos.responder.context.ExecutionContext;

/**
 * Created by Douglas Groves on 04/01/2017.
 */
public interface Flow {
    void execute(ExecutionContext context);
}