package com.temenos.responder.flows;

import com.temenos.responder.context.ExecutionContext;

/**
 * Created by Douglas Groves on 04/01/2017.
 */
public interface Flow {

    /**
     * Performs one or more actions contributing towards a defined workflow.
     *
     * @param context An {@link ExecutionContext execution context} instance. All flow parameters should be passed in using
     * {@link ExecutionContext#setAttribute(java.lang.String, java.lang.Object) the setAttribute method}.
     */
    void execute(ExecutionContext context);
}