package com.temenos.responder.context;

import com.temenos.responder.adapter.AdapterClient;
import com.temenos.responder.entity.runtime.Entity;

public class AdapterInvoker<T extends AdapterClient> {
    private Class<T> adapterType;
    private ExecutionContext executionContext;
    private T parameters;

    protected AdapterInvoker(Class<T> adapterType, ExecutionContext executionContext) {
        this.adapterType = this.adapterType;
        this.executionContext = executionContext;
    }

    public AdapterInvoker parameters(T parameters) {
        this.parameters = parameters;
        return this;
    }

    public Entity invoke() {
        return executionContext.getAdapterDispatcher().invokeAdapter(adapterType, parameters);
    }

}
