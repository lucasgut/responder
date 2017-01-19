package com.temenos.responder.commands.dashboard

import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 * A stub of a resource that returns account data as a
 * {@link com.temenos.responder.scaffold.dashboard.T24AccountInformation scaffold of a T24 account}.
 *
 * @author aburgos
 */
class T24AccountInformation implements Command {

    @Override
    void execute(CommandContext context) {
        try {
            def fromDirective = ['accountId']
            def intoDirective = 'finalResult'

            String accountId = context.getAttribute(fromDirective[0])
            // mock T24 customer information
            Map<String, Object> map = new HashMap<>()
            if(accountId == "1001") {
                map.put("ID", 1001)
                map.put("LABEL", "Savings")
                map.put("NUMBER", "GB29 NWBK 6016 1331 9268 19")
                map.put("BALANCE", 1200000)
                map.put("STANDING.ORDERS", [:])
            } else if(accountId == "1002") {
                map.put("ID", 1002)
                map.put("LABEL", "Daily account")
                map.put("NUMBER", "GB29 NWBK 6016 1331 9268 53")
                map.put("BALANCE", 8000)
                map.put("STANDING.ORDERS", ["200"])
            } else if(accountId == "1003") {
                map.put("ID", 1003)
                map.put("LABEL", "Dubious account")
                map.put("NUMBER", "VG96 VPVG 0000 0123 4567 8901")
                map.put("BALANCE", 68000000)
                map.put("STANDING.ORDERS", [:])
            } else if(accountId == "1004") {
                map.put("ID", 1004)
                map.put("LABEL", "Payments account")
                map.put("NUMBER", "DE89 3704 0044 0532 0130 00")
                map.put("BALANCE", 500000)
                map.put("STANDING.ORDERS", ["400", "401", "402"])
            } else if(accountId == "1009") {
                map.put("ID", 1009)
                map.put("LABEL", "H funding account")
                map.put("NUMBER", "LB62 0999 0000 0001 0019 0122 9114")
                map.put("BALANCE", 9620000)
                map.put("STANDING.ORDERS", [:])
            }

            context.setResponseCode(Response.Status.OK.statusCode as String)
            context.setAttribute(intoDirective, new Entity(map))
        } catch(IOException exception) {
            context.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            context.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}