package com.temenos.responder.flows.dashboard;

import com.temenos.responder.adapter.AdapterParameters;
import com.temenos.responder.adapter.t24.T24AdapterIdentifier;
import com.temenos.responder.adapter.t24.T24GetEntityAdapter;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.T24Flow;

public class GetT24CustomerFlow extends T24Flow {

    @Override
    public void doExecute(ExecutionContext executionContext) {
        String customerId = (String) executionContext.getFlowParameter("customerId");

        // Execute adapter
        AdapterParameters adapterParameters = T24GetEntityAdapter.requestBuilder()
                .t24EnquiryName("CUSTOMER.ENQUIRY")
                .id(customerId)
                .build();
        Entity response = invokeT24Adapter(executionContext, T24AdapterIdentifier.GetEntity, adapterParameters);

        executionContext.setFlowResponse(response);
        executionContext.setAttribute("status", "OK");
    }
}