package com.temenos.responder.flows

import com.temenos.responder.commands.Command
import com.temenos.responder.commands.transformers.CustomerDashboardTransformer
import com.temenos.responder.commands.transformers.CustomerTransformer

import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 * This flow retrieves a customer record from an external resource and
 * maps its fields to a consumer model representation using {@link CustomerTransformer a transformer}.
 *
 * Created by aburgos on 17/01/2017.
 */
class CustomerDashboardGetMainFlow_1_0 extends AbstractFlow {

    @Override
    public void doExecute(ExecutionContext executionContext) {
        try {
            def from = ['customerId']
            def into = 'finalResult'

            // get external customer information
            Command getExternalCustomer = executionContext.getCommand(ExternalCustomerDashboard.class)

            //setup command context
            CommandContext commandContext = new DefaultCommandContext()
            commandContext.setAttribute(from[0], executionContext.getAttribute(from[0]))

            //execute the command
            getExternalCustomer.execute(commandContext)

            // check result
            Entity extnCustomer = commandContext.getAttribute('finalResult') as Entity

            //TODO: use an exception instead
            if (!commandContext.getResponseCode().equals(Response.Status.OK.statusCode as String)) {
                executionContext.setResponseCode(commandContext.getResponseCode() as String)
                executionContext.setAttribute(into, new Entity())
                return;
            }

            if(!extnCustomer.getEntityNames().empty) {
                // transform external customer model into internal customer model
                Command transformer = executionContext.getCommand(CustomerDashboardTransformer.class)
                commandContext = new DefaultCommandContext(['extnCustomer'], 'finalResult')
                commandContext.setAttribute('extnCustomer', extnCustomer)
                transformer.execute(commandContext)
            }

            Entity responseBody = commandContext.getAttribute('finalResult') as Entity;
            executionContext.setResponseCode(Response.Status.OK.statusCode as String)
            executionContext.setAttribute(into, responseBody)
        } catch (IOException exception) {
            executionContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}