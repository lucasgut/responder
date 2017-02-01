package com.temenos.responder.commands.dashboard

import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 * A stub of a resource that returns customer data as a
 * {@link com.temenos.responder.scaffold.dashboard.ScaffoldT24CustomerInformation scaffold of an external customer}.
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
                map.put("HOME.ADDRESS", ["LINE1": "No Name Street", "LINE2": "", "POSTCODE": "NW9 6LR"])
                map.put("WORK.ADDRESS", ["LINE1": "85 Albert Embankment", "LINE2": "Lambeth", "POSTCODE": "SE1 1BD"])
                map.put("RELATIVES", [["NAME": "Jim Cain", "RELATIONSHIP": "Father"], ["NAME": "Rick Perry", "RELATIONSHIP": "Sibling"]])
                map.put("ACCOUNTS", ["1001", "1004", "1009"])
            } else if(customerId == "100200") {
                map.put("ID", 100200)
                map.put("NAME", "Iris Law")
                map.put("HOME.ADDRESS", ["LINE1": "2 Lansdowne Rd", "LINE2": "", "POSTCODE": "CR8 2PA"])
                map.put("WORK.ADDRESS", ["LINE1": "9 Argyll Street", "LINE2": "", "POSTCODE": "SE1 9TG"])
                map.put("RELATIVES", [["NAME": "Jeff Barry", "RELATIONSHIP": "Father"], ["NAME": "T Mayhem", "RELATIONSHIP": "Mother"]])
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