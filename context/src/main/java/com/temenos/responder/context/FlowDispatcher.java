package com.temenos.responder.context;

import com.temenos.responder.adapter.AdapterException;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.Flow;

import java.util.HashMap;
import java.util.Map;

public class FlowDispatcher {
    private final Map<String, Flow> flows = new HashMap<>();

    public <T extends FlowParameters> Entity invokeFlow(String flowId, T flowParameters) throws AdapterException {
        try {
            executionContext.setFlowParameters(flowParameters);
            return flows.get(flowId).execute(executionContext);
        } catch(Exception e) {
            throw new AdapterException(e);
        }

    }}
