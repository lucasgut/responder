package com.temenos.responder.commands.transformers

import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldCustomer
import com.temenos.responder.scaffold.ScaffoldExternalCustomer

import javax.ws.rs.core.Response

/**
 * Converts an entity whose data structure conforms to
 * {@link ScaffoldExternalCustomer} into an {@link Entity entity}
 * conforming to {@link com.temenos.responder.scaffold.ScaffoldCustomer}.
 *
 * @author Douglas Groves
 */
public class CustomerTransformer implements Command {

    @Override
    void execute(CommandContext context) {
        //fetch entity from command context
        def entity = context.getAttribute(context.from()[0]) as Entity

        // transform external customer model into internal customer model
        Entity responseBody = new Entity([
                (ScaffoldCustomer.CUSTOMER_ID) : entity.get(ScaffoldExternalCustomer.CUSTOMER_ID),
                (ScaffoldCustomer.CUSTOMER_NAME) : entity.get(ScaffoldExternalCustomer.CUSTOMER_NAME),
                (ScaffoldCustomer.CUSTOMER_ADDRESS) : entity.get(ScaffoldExternalCustomer.CUSTOMER_ADDRESS)
        ])

        //construct response
        context.setResponseCode(Response.Status.OK.statusCode)
        context.setAttribute(context.into(), responseBody)
    }
}
