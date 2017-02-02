package com.temenos.responder.flows.dashboard;

import com.temenos.responder.adapter.AdapterResult;
import com.temenos.responder.adapter.t24.T24AdapterIdentifier;
import com.temenos.responder.adapter.t24.T24GetEntityAdapter;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.FlowException;
import com.temenos.responder.context.FlowResult;
import com.temenos.responder.flows.AbstractFlow;

import java.util.Date;

public class GetT24CustomerFlow extends AbstractFlow {

    @Override
    public FlowResult doExecute(ExecutionContext flowContext) {
        String customerId = (String) flowContext.getFlowParameter("customerId");

        AdapterResult adapterResult = flowContext
                .adapter(T24AdapterIdentifier.GetEntity)
                .parameters(T24GetEntityAdapter.requestBuilder()
                        .t24EnquiryName("CUSTOMER.ENQUIRY")
                        .id(customerId)
                        .build())
                .invoke();
        if(!adapterResult.isSuccess()) {
            throw new FlowException(convertT24AdapterToHttpError(adapterResult.getErrorCode()), adapterResult.getErrorMessage());
        }
        return FlowResult.builder()
                .entity(adapterResult.getEntity())
                .attribute("time", new Date())
                .build();
    }

    private int convertT24AdapterToHttpError(int errorCode) {
        return 0;
    }
}