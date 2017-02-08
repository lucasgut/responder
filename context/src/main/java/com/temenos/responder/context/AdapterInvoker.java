package com.temenos.responder.context;

import com.temenos.responder.adapter.AdapterParameters;
import com.temenos.responder.adapter.AdapterResult;

import java.util.Map;

public class AdapterInvoker<T extends AdapterParameters> {
    private Class<T> adapterType;
    private ExecutionContext executionContext;
    private T parameters;
    private Map<String, Object> attributes;

    protected AdapterInvoker(Class<T> adapterType, ExecutionContext executionContext) {
        this.adapterType = adapterType;
        this.executionContext = executionContext;
    }

    public AdapterInvoker parameters(T parameters) {
        this.parameters = parameters;
        return this;
    }

    public AdapterResult invoke() {
        return executionContext.getAdapterDispatcher().invokeAdapter(adapterType, parameters, executionContext);
    }

    public AdapterInvoker attributes(Map<String, Object> attributes) {
        this.attributes = attributes;
        return this;
    }
}
