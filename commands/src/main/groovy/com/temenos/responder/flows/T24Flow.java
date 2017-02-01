package com.temenos.responder.flows;

import com.temenos.responder.adapter.AdapterParameters;
import com.temenos.responder.adapter.t24.T24AdapterException;
import com.temenos.responder.adapter.t24.T24AdapterIdentifier;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.entity.runtime.Entity;

public abstract class T24Flow extends AbstractFlow {

    protected Entity invokeT24Adapter(ExecutionContext executionContext, T24AdapterIdentifier adapterIdentifier, AdapterParameters adapterParameters) {
        try {
            return executionContext
                    .getAdapterDispatcher()
                    .invokeAdapter(adapterIdentifier, adapterParameters);
        } catch(T24AdapterException e) {
            throw new FlowException(400, e.getMessage());
        } catch(Exception e) {
            throw new FlowException(500, e.getMessage());
        }
    }
}