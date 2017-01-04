package com.temenos.responder.flows

import com.temenos.responder.commands.Command
import com.temenos.responder.commands.ExternalCustomerInformation
import com.temenos.responder.commands.transformers.CustomerTransformer
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.DefaultExecutionContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 *
 * Created by aburgos on 23/12/2016.
 */
class CustomerInformation implements Flow {

    @Override
    def execute(ExecutionContext executionContext) {
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
            Entity extnCustomer = (Entity) commandContext.getAttribute('finalResult')
            
            //TODO: use an exception instead
            if (!commandContext.getResponseCode().equals(Response.Status.OK.statusCode as String)) {
                executionContext.setResponseCode(commandContext.getResponseCode() as String)
                executionContext.setAttribute(into, new Entity())
                return;
            }

            // transform external customer model into internal customer model
            Command transformer = executionContext.getCommand(CustomerTransformer.class)
            commandContext = new DefaultCommandContext([from: ['ExtnCustomer'], into: 'finalResult'])
            commandContext.setAttribute('ExtnCustomer', extnCustomer)
            transformer.execute(commandContext)

            Entity responseBody = commandContext.getAttribute('finalResult') as Entity;
            executionContext.setResponseCode(Response.Status.OK.statusCode as String)
            executionContext.setAttribute(into, responseBody)
        } catch (IOException exception) {
            executionContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}