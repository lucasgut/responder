package com.temenos.responder.flows

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.context.Parameters
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
        context.buildExecution()
                .flow(CustomerInformation)
                .parameter('id',context.getAttribute('CustomerId') as String)
                .into('custInfo')
                .execution()
            .inParallelWith(CustomerAddressFlow)
                .parameter('AddressId', context.getAttribute('AddressId') as String)
                .into('custAddress')
                .execution()
            .execute();
        def result = new Entity([
                (ScaffoldComplexCustomer.CUSTOMER_NAME)     : context.getAttribute('custInfo').getBody().get(ScaffoldCustomer.CUSTOMER_NAME),
                (ScaffoldComplexCustomer.CUSTOMER_ADDRESSES): context.getAttribute('custAddress').getBody().get(ScaffoldAddress.ADDRESSES)
        ])
        context.setAttribute("finalResult", result)
        context.setResponseCode(200)
    }
}
