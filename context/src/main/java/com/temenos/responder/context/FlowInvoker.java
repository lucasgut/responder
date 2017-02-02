package com.temenos.responder.context;

import rx.Observable;

import java.util.HashMap;
import java.util.Map;

public class FlowInvoker {

    private String flowName;
    private ExecutionContext executionContext;
    private Map<String, Object> parameters = new HashMap<>();

    FlowInvoker(String flowName, ExecutionContext executionContext) {
        this.flowName = flowName;
        this.executionContext = executionContext;
    }

    public FlowInvoker parameter(String name, Object value) {
        this.parameters.put(name, value);
        return this;
    }

    public FlowResult invoke() {
        return executionContext.getFlowDispatcher().invokeFlow(flowName, parameters, executionContext);
    }

    public Observable observable() {
        return Observable.fromCallable(() -> executionContext.getFlowDispatcher().invokeFlow(flowName, parameters, executionContext));
    }
}
