package com.temenos.responder.commands.dashboard

import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 * A stub of a resource that returns standing order data as a
 * {@link com.temenos.responder.scaffold.dashboard.ScaffoldT24StandingOrder scaffold of a T24 standing order}.
 *
 * @author aburgos
 */
class T24StandingOrder implements Command {

    @Override
    void execute(CommandContext context) {
        try {
            def fromDirective = ['standingOrderId']
            def intoDirective = 'finalResult'

            String standingOrderId = context.getAttribute(fromDirective[0])
            // mock T24 standing orders
            Map<String, Object> map = new HashMap<>()
            if(standingOrderId == "200") {
                map.put("ID", 200)
                map.put("TARGET.ACCOUNT", "GB91 BKEN 1000 0041 6100 08")
                map.put("AMOUNT", 1200.0)
                map.put("TRANSACTION.DATE", "2001-01-26 11:58:29")
            } else if(standingOrderId == "400") {
                map.put("ID", 400)
                map.put("TARGET.ACCOUNT", "GB27 BOFI 9021 2729 8235 29")
                map.put("AMOUNT", 2020.0)
                map.put("TRANSACTION.DATE", "2008-05-14 16:02:50")
            } else if(standingOrderId == "401") {
                map.put("ID", 401)
                map.put("TARGET.ACCOUNT", "GB29 NWBK 6016 1331 9268 19")
                map.put("AMOUNT", 2000.0)
                map.put("TRANSACTION.DATE", "2011-07-30 12:56:23")
            } else if(standingOrderId == "402") {
                map.put("ID", 402)
                map.put("TARGET.ACCOUNT", "GB29 NWBK 6016 1331 9268 53")
                map.put("AMOUNT", 4000.0)
                map.put("TRANSACTION.DATE", "1995-11-26 15:20:52")
            }

            context.setResponseCode(Response.Status.OK.statusCode)
            context.setAttribute(intoDirective, new Entity(map))
        } catch(IOException exception) {
            context.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode)
            context.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}