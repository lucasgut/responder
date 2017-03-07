package com.temenos.responder.context;

import com.temenos.responder.adapter.AdapterResult;

import java.util.Map;

public class AdapterInvoker {
    private String adapterCommand;
    private ExecutionContext executionContext;
    private Map<String, Object> parameters;

    protected AdapterInvoker(String adapterCommand, ExecutionContext executionContext) {
        this.adapterCommand = adapterCommand;
        this.executionContext = executionContext;
    }

    public AdapterResult invoke() {
        return executionContext.getAdapterDispatcher().invokeAdapter(adapterCommand, parameters, executionContext);
    }

    public AdapterInvoker parameter(String name, String value) {
        this.parameters.put(name, value);
        return this;
    }

    public AdapterInvoker parameters(Map<String, Object> parameters) {
        this.parameters = parameters;
        return this;
    }
}
