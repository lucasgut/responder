package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 * Created by aburgos on 23/12/2016.
 */
class ExternalCustomerInformation implements Command {

    @Override
    void execute(CommandContext executionContext) {
        try {
            def fromDirective = ['id']
            def intoDirective = 'finalResult'

            String customerId = executionContext.getAttribute(fromDirective[0])
            // mock T24 customer information
            Map<String, String> map = new HashMap<>()
            if(customerId == "100100") {
                map.put("CUSTOMER_ID", 100100)
                map.put("CUSTOMER_NAME", "John Smith")
                map.put("CUSTOMER_ADDRESS", "No Name Street")
            } else if(customerId == "100200") {
                map.put("CUSTOMER_ID", 100200)
                map.put("CUSTOMER_NAME", "Iris Law")
                map.put("CUSTOMER_ADDRESS", "2 Lansdowne Rd")
            } else {
                executionContext.setResponseCode(Response.Status.NOT_FOUND.statusCode as String)
                executionContext.setAttribute(intoDirective, new Entity())
                return;
            }
            Entity responseBody = new Entity(map);
            executionContext.setResponseCode(Response.Status.OK.statusCode as String)
            executionContext.setAttribute(intoDirective, responseBody)
        } catch(IOException exception) {
            executionContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}