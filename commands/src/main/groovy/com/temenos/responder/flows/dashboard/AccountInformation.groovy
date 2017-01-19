package com.temenos.responder.flows.dashboard

import com.temenos.responder.commands.Command
import com.temenos.responder.commands.dashboard.T24AccountInformation
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.flows.AbstractFlow

import javax.ws.rs.core.Response

/**
 * @author Andres Burgos
 */
class AccountInformation extends AbstractFlow {

    @Override
    public void doExecute(ExecutionContext executionContext) {
        try {
            def from = ['accountId']
            def into = 'finalResult'

            // get external customer information
            Command getAccountInfo = executionContext.getCommand(T24AccountInformation.class)

            //setup command context
            CommandContext commandContext = new DefaultCommandContext()
            commandContext.setAttribute(from[0], executionContext.getAttribute(from[0]))

            //execute the command
            getAccountInfo.execute(commandContext)

            // check result
            Entity accInfo = commandContext.getAttribute('finalResult') as Entity

            //TODO: use an exception instead
            if (!commandContext.getResponseCode().equals(Response.Status.OK.statusCode as String)) {
                executionContext.setResponseCode(commandContext.getResponseCode() as String)
                executionContext.setAttribute(into, new Entity())
                return;
            }

            executionContext.setResponseCode(Response.Status.OK.statusCode as String)
            executionContext.setAttribute(into, accInfo)
        } catch (IOException exception) {
            executionContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}