package com.temenos.responder.flows.dashboard;

import com.google.common.collect.ImmutableMap;
import com.temenos.responder.adapter.AdapterResult;
import com.temenos.responder.adapter.t24.T24GetEntityAdapterParameters;
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
    public Entity doExecute(ExecutionContext flowContext) {
        String customerId = flowContext.getQueryParameter("id");

        // Get T24 customer
        AdapterResult result = flowContext
                .adapterCommand(T24GetEntityAdapterParameters.class)
                .parameters(T24GetEntityAdapterParameters.builder()
                        .t24EnquiryName("CUSTOMER.ENQUIRY")
                        .id(customerId)
                        .build())
                .attributes(ImmutableMap.of("MY_ATTRIBUTE", "Hello"))
                .invoke();

        // Transform T24 customer
        return flowContext
                .flow("TransformT24CustomerFlow")
                .parameter("customer", result.getEntity())
                .parameter("locale", result.getAttributes().get("MY_LOCALE"))
                .invoke();
    }
}