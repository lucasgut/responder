package com.temenos.responder.commands.dashboard

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
class T24CustomerInformation implements Command {

    @Override
    void execute(CommandContext context) {
        try {
            def fromDirective = ['customerId']
            def intoDirective = 'finalResult'

            String customerId = context.getAttribute(fromDirective[0])
            // mock T24 customer information
            Map<String, Object> map = new HashMap<>()
            if(customerId == "100100") {
                map.put("ID", 100100)
                map.put("NAME", "John Smith")
                map.put("HOME.ADDRESS", "No Name Street")
                map.put("WORK.ADDRESS", "85 Albert Embankment")
                map.put("RELATIVES", ["0": ["NAME": "Jim Cain", "RELATIONSHIP": "Father"], "1": ["NAME": "Rick Perry", "RELATIONSHIP": "Sibling"]])
                map.put("ACCOUNTS", ["1001", "1004", "1009"])
            } else if(customerId == "100200") {
                map.put("ID", 100200)
                map.put("NAME", "Iris Law")
                map.put("HOME.ADDRESS", "2 Lansdowne Rd")
                map.put("WORK.ADDRESS", "9 Argyll Street")
                map.put("RELATIVES", ["0": ["NAME": "Jeff Barry", "RELATIONSHIP": "Father"], "1": ["NAME": "T Mayhem", "RELATIONSHIP": "Mother"]])
                map.put("ACCOUNTS", ["1002", "1003"])
            }

            context.setResponseCode(Response.Status.OK.statusCode as String)
            context.setAttribute(intoDirective, new Entity(map))
        } catch(IOException exception) {
            context.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            context.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}