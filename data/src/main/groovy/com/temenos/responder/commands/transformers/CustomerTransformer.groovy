package com.temenos.responder.commands.transformers

import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldCustomer
import com.temenos.responder.scaffold.ScaffoldT24Customer

import javax.ws.rs.core.Response

/**
 * Created by Douglas Groves on 04/01/2017.
 */
class CustomerTransformer implements Command {

    @Override
    def execute(CommandContext context) {
        //fetch entity from command context
        def from = context.getAttribute('from') as List<String>
        def into = context.getAttribute('into')
        def entity = context.getAttribute(from.first()) as Entity

        // transform external customer model into internal customer model
        def map = [:]
        map.put(ScaffoldCustomer.CUSTOMER_ID.name, entity.get(ScaffoldT24Customer.CUSTOMER_ID.name))
        map.put(ScaffoldCustomer.CUSTOMER_NAME.name, entity.get(ScaffoldT24Customer.CUSTOMER_NAME.name))
        map.put(ScaffoldCustomer.CUSTOMER_ADDRESS.name, entity.get(ScaffoldT24Customer.CUSTOMER_ADDRESS.name))
        Entity responseBody = new Entity(map)

        //construct response
        context.setResponseCode(Response.Status.OK.statusCode as String)
        context.setAttribute(into, responseBody)
    }
}
