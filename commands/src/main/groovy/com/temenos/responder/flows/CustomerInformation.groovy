package com.temenos.responder.flows

import com.temenos.responder.commands.Command
import com.temenos.responder.commands.ExternalCustomerInformation
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.scaffold.ScaffoldCustomer
import com.temenos.responder.scaffold.ScaffoldExternalCustomer

import javax.ws.rs.core.Response

/**
 *
 * Created by aburgos on 23/12/2016.
 */
class CustomerInformation extends AbstractFlow {

    @Override
    public void doExecute(ExecutionContext executionContext) {
        try {
            def from = ['id']
            def into = 'finalResult'

            // get external customer information
            Command getExternalCustomer = executionContext.getCommand(ExternalCustomerInformation.class)

            //setup command context
            CommandContext commandContext = new DefaultCommandContext()
            commandContext.setAttribute('id', executionContext.getAttribute(from[0]))

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

            // transform external customer model into internal customer model
            Entity responseBody = new Entity();
            responseBody.set(ScaffoldCustomer.CUSTOMER_ID, extnCustomer.get(ScaffoldExternalCustomer.CUSTOMER_ID));
            responseBody.set(ScaffoldCustomer.CUSTOMER_NAME, extnCustomer.get(ScaffoldExternalCustomer.CUSTOMER_NAME));
            responseBody.set(ScaffoldCustomer.CUSTOMER_ADDRESS, extnCustomer.get(ScaffoldExternalCustomer.CUSTOMER_ADDRESS));

            executionContext.setResponseCode(Response.Status.OK.statusCode as String)
            executionContext.setAttribute(into, responseBody)
        } catch (IOException exception) {
            executionContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}