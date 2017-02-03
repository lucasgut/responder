package com.temenos.responder.flows.dashboard;

import com.temenos.responder.adapter.t24.T24GetEntityAdapterClient;
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
        Entity customer = flowContext
                .adapter(T24GetEntityAdapterClient.class)
                .parameters(T24GetEntityAdapterClient.builder()
                        .t24EnquiryName("CUSTOMER.ENQUIRY")
                        .id(customerId)
                        .build())
                .invoke();

        // Transform T24 customer
        return flowContext
                .flow("TransformT24CustomerFlow")
                .parameter("customer", customer)
                .parameter("locale", "en_GB")
                .invoke();
    }

}