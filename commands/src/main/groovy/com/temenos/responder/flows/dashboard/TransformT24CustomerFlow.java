package com.temenos.responder.flows.dashboard;

import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.FlowException;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.AbstractFlow;

public class TransformT24CustomerFlow extends AbstractFlow {

    @Override
    public Entity doExecute(ExecutionContext executionContext) {
        Entity customer = executionContext.getFlowParameterAsEntity("customer");

        // transform
        if(customer.get("id") == null) {
            throw new FlowException(500, "internal server error", null);
        }

        return new Entity();
    }
}