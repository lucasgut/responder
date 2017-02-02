package com.temenos.responder.flows.dashboard;

import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.FlowResult;
import com.temenos.responder.flows.AbstractFlow;
import lombok.extern.slf4j.Slf4j;

/**
 * This flow retrieves customer information from T24, comprising its accounts and their
 * respective standing orders.
 */
@Slf4j
public class EnrichCustomerDashboardFlow_4_0 extends AbstractFlow {

    @Override
    public FlowResult doExecute(ExecutionContext flowContext) {
        FlowResult flowResult = flowContext
                .flow("GetT24CustomerFlow")
                .parameter("customerId", flowContext.getQueryParameter("id"))
                .parameter("locale", "en_GB")
                .invoke();
        int time = (int) flowResult.getAttributes().get("time");
        //log.info("GetEntity flow finished at: ", time);

        return flowResult;
    }

}