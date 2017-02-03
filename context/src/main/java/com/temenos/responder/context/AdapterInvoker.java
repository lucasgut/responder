package com.temenos.responder.context;

import com.temenos.responder.adapter.AdapterClient;
import com.temenos.responder.adapter.AdapterIdentifier;
import com.temenos.responder.entity.runtime.Entity;

public class AdapterInvoker<T extends AdapterClient> {
    private AdapterIdentifier adapterIdentifier;
    private ExecutionContext executionContext;
    private T parameters;

    protected AdapterInvoker(AdapterIdentifier adapterIdentifier, ExecutionContext executionContext) {
        this.adapterIdentifier = adapterIdentifier;
        this.executionContext = executionContext;
    }

    public AdapterInvoker parameters(T parameters) {
        this.parameters = parameters;
        return this;
    }

    public Entity invoke() {
        return executionContext.getAdapterDispatcher().invokeAdapter(adapterIdentifier, parameters);
    }

}
