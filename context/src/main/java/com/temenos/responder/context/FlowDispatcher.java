package com.temenos.responder.context;

import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.Flow;

import java.util.HashMap;
import java.util.Map;

public class FlowDispatcher {
    private final Map<String, Flow> flows = new HashMap<>();

    public Entity invokeFlow(String flowId, Map<String, Object> flowParameters, ExecutionContext executionContext) {
        try {
            Flow flow = flows.get(flowId);
            executionContext.setFlowParameters(flowParameters);
            return flow.execute(executionContext);
        } catch (Exception e) {
            throw new FlowException(500, e.getMessage(), null);
        }
    }
}
