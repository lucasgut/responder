package com.temenos.responder.flows

import com.temenos.responder.commands.AdditionCommand
import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext

/**
 * Created by Douglas Groves on 04/01/2017.
 */
class AdditionFlow implements Flow {

    @Override
    def execute(ExecutionContext context) {
        //fetch command
        Command command = context.getCommand(AdditionCommand)

        //create command context
        CommandContext commandContext = new DefaultCommandContext()

        //set 'from' attribute
        commandContext.setAttribute("from", context.getRequestBody().get("operands"))

        //set 'into' attribute
        commandContext.setAttribute("into", 'finalResult')

        //execute command
        command.execute(commandContext)

        //pass command output back to execution context and set response code
        context.setAttribute("finalResult", commandContext.getAttribute("finalResult"))
        context.setResponseCode(commandContext.getResponseCode())
    }
}
