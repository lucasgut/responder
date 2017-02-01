package com.temenos.responder.flows

import com.temenos.responder.commands.Command
import com.temenos.responder.commands.Embed
import com.temenos.responder.commands.GETResource
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Document
import com.temenos.responder.entity.runtime.Entity

/**
 * Created by dgroves on 20/01/2017.
 */
class CustomerAddressEmbed extends AbstractFlow {

    @Override
    void doExecute(ExecutionContext context) {
        //fetch customer addresses
        Document customerAddresses = context.notifyDispatchers(CustomerAddressFlow)

        String addressId = context.getAttribute("AddressId")

        //fetch another resource
        CommandContext ctx = new DefaultCommandContext()
        ctx.from([context.getInternalResource("address/"+addressId),'','application/json'])
        ctx.into('finalResult')
        context.getCommand(GETResource).execute(ctx)
        def result = ctx.getAttribute("finalResult")

        //embed data in the document
        ctx = new DefaultCommandContext()
        ctx.setAttribute('address', result)
        ctx.from(['CustomerAddresses','address'])
        ctx.into('finalResult')
        context.getCommand(Embed).execute(ctx)
        context.setAttribute("document.embedded", ((Entity)ctx.getAttribute("finalResult")))

        //fetch customer data
        Document customerData = context.notifyDispatchers(CustomerInformation)
        context.setAttribute("finalResult", customerData.getBody())
        context.setResponseCode(200)
    }
}
