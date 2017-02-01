package com.temenos.responder.flows.dashboard;

import com.google.common.collect.ImmutableMap;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.AbstractFlow;
import lombok.extern.slf4j.Slf4j;

/**
 * This flow retrieves customer information from T24, comprising its accounts and their
 * respective standing orders.
 */
@Slf4j
public class EnrichCustomerDashboardFlow_3_0 extends AbstractFlow {

    @Override
    public void doExecute(ExecutionContext executionContext) {
        final Entity customerDashboard2Output = executionContext.getFlowResponse(CustomerDashboardGetMainFlow_2_0.class);


        Entity t24CustomerEntity = executionContext.getFlowDispatcher()
                .from("abc", "xyz")
                .into("status")
                .invoke(GetT24CustomerFlow.class,
                ImmutableMap.of("customerId" , executionContext.getQueryParameter("id")));
        int status = executionContext.getAttribute("status");
        log.info("get customer status: ", status);

        // Create response entity
        Entity response = new Entity();
        response.set("dashboard", customerDashboard2Output);
        response.set("address", t24CustomerEntity.get("address"));

        executionContext.setFlowResponse(response);
    }
}