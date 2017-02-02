package com.temenos.responder.context;

import com.temenos.responder.flows.Flow;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class FlowDispatcher {
    private final Map<String, Flow> flows = new HashMap<>();

    public FlowResult invokeFlow(String flowId, Map<String, Object> flowParameters, ExecutionContext executionContext) {
        try {
            Flow flow = flows.get(flowId);
            executionContext.setFlowParameters(flowParameters);
            return flow.execute(executionContext);
        } catch (FlowException e) {
            return FlowResult.builder()
                    .status(e.getStatus())
                    .errorMessage(e.getMessage())
                    .build();
        } catch (Exception e) {
            return FlowResult.builder()
                    .status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode())
                    .errorMessage(e.getMessage())
                    .build();
        }
    }
}
