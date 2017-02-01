package com.temenos.responder.flows

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldAddress
import com.temenos.responder.scaffold.ScaffoldComplexCustomer
import com.temenos.responder.scaffold.ScaffoldCustomer

/**
 * Created by dgroves on 19/01/2017.
 */
class ComplexCustomerInformation extends AbstractFlow {

    @Override
    void doExecute(ExecutionContext context) {
        def outputs = context.notifyDispatchers([CustomerInformation, CustomerAddressFlow])
        def result = new Entity([
                (ScaffoldComplexCustomer.CUSTOMER_NAME)     : outputs[CustomerInformation.simpleName][0].getBody().get(ScaffoldCustomer.CUSTOMER_NAME),
                (ScaffoldComplexCustomer.CUSTOMER_ADDRESSES): outputs[CustomerAddressFlow.simpleName][0].getBody().get(ScaffoldAddress.ADDRESSES)
        ])
        context.setAttribute("finalResult", result)
        context.setResponseCode(200)
    }
}
