package com.temenos.responder.flows.dashboard;

import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.FlowException;
import com.temenos.responder.context.FlowResult;
import com.temenos.responder.flows.AbstractFlow;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;

/**
 * This flow retrieves customer information from T24, comprising its accounts and their
 * respective standing orders.
 */
@Slf4j
public class EnrichCustomerDashboardFlow_3_0 extends AbstractFlow {

    @Override
    public FlowResult doExecute(ExecutionContext executionContext) {
        FlowResult flowResult = executionContext
                .flow("GetT24CustomerFlow")
                .parameter("customerId", executionContext.getQueryParameter("id"))
                .parameter("locale", "en_GB")
                .invoke();
        if(!flowResult.isSuccess()) {
            throw new FlowException(flowResult.getStatus(), flowResult.getErrorMessage());
        }
        int time = (int) flowResult.getAttributes().get("time");
        //log.info("GetEntity flow finished at: ", time);

        return FlowResult.builder()
                .status(Response.Status.OK.getStatusCode())
                .entity(flowResult.getEntity())
                .build();
    }
}