package com.temenos.responder.flows

import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 * A stub of a resource that returns customer data as a
 * {@link com.temenos.responder.scaffold.ScaffoldExternalCustomer scaffold of an external customer}.
 *
 * @author aburgos
 */
class ExternalCustomerDashboard implements Command {

    @Override
    void execute(CommandContext context) {
        try {
            def fromDirective = ['customerId']
            def intoDirective = 'finalResult'

            String customerId = context.getAttribute(fromDirective[0])
            // mock T24 customer information
            Map<String, Object> map = new HashMap<>()
            if(customerId == "100100") {
                map.put("CUSTOMER.ID", 100100)
                map.put("CUSTOMER.NAME", "John Smith")
                map.put("CUSTOMER.HOME.ADDRESS", "No Name Street")
                map.put("CUSTOMER.WORK.ADDRESS", "85 Albert Embankment")
                context.setResponseCode(Response.Status.OK.statusCode as String)
            } else if(customerId == "100200") {
                map.put("CUSTOMER.ID", 100200)
                map.put("CUSTOMER.NAME", "Iris Law")
                map.put("CUSTOMER.HOME.ADDRESS", "2 Lansdowne Rd")
                map.put("CUSTOMER.WORK.ADDRESS", "9 Argyll Street")
                context.setResponseCode(Response.Status.OK.statusCode as String)
            } else {
                map.put("ERROR.CODE", Response.Status.NOT_FOUND.statusCode)
                map.put("ERROR.TEXT", "No customer found")
                map.put("ERROR.INFO", "Unknown customer")
                context.setResponseCode(map.get("ERROR.CODE") as String)
            }
            context.setAttribute(intoDirective, new Entity(map))
        } catch(IOException exception) {
            context.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            context.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}