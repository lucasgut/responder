package com.temenos.responder.flows.dashboard;

import com.google.common.collect.ImmutableMap;
import com.temenos.responder.adapter.AdapterContext;
import com.temenos.responder.adapter.AdapterResult;
import com.temenos.responder.adapter.t24.T24Adapters;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.Link;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.AbstractFlow;
import lombok.extern.slf4j.Slf4j;
import rx.Observable;

import java.util.List;

import static com.temenos.responder.adapter.t24.T24Adapters.GetEntityParameters.ID;
import static com.temenos.responder.adapter.t24.T24Adapters.GetEntityParameters.T24_ENQUIRY_NAME;

/**
 * This flow retrieves customer information from T24, comprising its accounts and their
 * respective standing orders.
 */
@Slf4j
public class EnrichCustomerDashboardFlow_3_0 extends AbstractFlow {

    @Override
    public Entity doExecute(ExecutionContext flowContext) {
        String customerId = flowContext.getQueryParameter("id");

        Observable<AdapterResult> customers = Observable.fromCallable(() -> flowContext
                .useAdapter(T24Adapters.GET_ENTITY)
                .parameter(T24_ENQUIRY_NAME, "CUSTOMER.ENQUIRY")
                .invoke());
        customers.subscribe(customer -> flowContext
                .useAdapter(T24Adapters.GET_ENTITIES)
                .parameter(T24_ENQUIRY_NAME, "CUSTOMER.ENQUIRY")
                .parameter(ID, customerId)
                .invoke());


        // Get T24 customer
        AdapterResult result = ;

        List<Link> links =  flowContext.createLinks("Account", ImmutableMap.of("id", result.getEntity().get("accountId")));

        // Transform T24 customer
        return flowContext
                .flow("TransformT24CustomerFlow")
                .parameter("customer", result.getEntity())
                .parameter("locale", result.getAttributes().get("MY_LOCALE"))
                .invoke();
    }
}