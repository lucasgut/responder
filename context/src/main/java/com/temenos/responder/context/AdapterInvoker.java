package com.temenos.responder.context;

import com.temenos.responder.adapter.AdapterIdentifier;
import com.temenos.responder.adapter.AdapterParameters;
import com.temenos.responder.adapter.AdapterResult;

public class AdapterInvoker {
    private AdapterIdentifier adapterIdentifier;
    private ExecutionContext executionContext;
    private AdapterParameters parameters;

    AdapterInvoker(AdapterIdentifier adapterIdentifier, ExecutionContext executionContext) {
        this.adapterIdentifier = adapterIdentifier;
        this.executionContext = executionContext;
    }

    public AdapterInvoker parameters(AdapterParameters parameters) {
        this.parameters = parameters;
        return this;
    }

    public AdapterResult invoke() {
        return executionContext.getAdapterDispatcher().invokeAdapter(adapterIdentifier, parameters);
    }

}
