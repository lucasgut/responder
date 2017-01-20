package com.temenos.responder.commands.transformers

import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldCustomerDashboard_1
import com.temenos.responder.scaffold.ScaffoldExternalCustomer
import com.temenos.responder.scaffold.ScaffoldExternalCustomerDashboard

import javax.ws.rs.core.Response

/**
 * Converts an entity whose data structure conforms to
 * {@link ScaffoldExternalCustomerDashboard} into an {@link Entity entity}
 * conforming to {@link com.temenos.responder.scaffold.ScaffoldCustomerDashboard_1}.
 *
 * @author Andres Burgos
 */
public class CustomerDashboardTransformer implements Command {

    @Override
    void execute(CommandContext context) {
        def fromDirective = context.from()[0]
        def intoDirective = context.into()

        //fetch entity from command context
        def entity = context.getAttribute(fromDirective) as Entity

        // transform external customer model into internal customer model
        Entity responseBody = new Entity([
                (ScaffoldCustomerDashboard_1.CUSTOMER_ID)          : entity.get(ScaffoldExternalCustomerDashboard.CUSTOMER_ID),
                (ScaffoldCustomerDashboard_1.CUSTOMER_NAME)        : entity.get(ScaffoldExternalCustomerDashboard.CUSTOMER_NAME),
                (ScaffoldCustomerDashboard_1.CUSTOMER_HOME_ADDRESS): entity.get(ScaffoldExternalCustomerDashboard.CUSTOMER_HOME_ADDRESS),
                (ScaffoldCustomerDashboard_1.CUSTOMER_WORK_ADDRESS): entity.get(ScaffoldExternalCustomerDashboard.CUSTOMER_WORK_ADDRESS)
        ])

        //construct response
        context.setResponseCode(Response.Status.OK.statusCode as String)
        context.setAttribute(intoDirective, responseBody)
    }
}
