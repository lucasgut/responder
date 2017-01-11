package com.temenos.responder.flows

import com.temenos.responder.commands.AdditionCommand
import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.scaffold.ScaffoldAdditionInput

/**
 * This flow sums a list of operands and inserts the calculated result into the
 * {@link com.temenos.responder.context.ExecutionContext execution context}.
 *
 * @author Douglas Groves
 */
class AdditionFlow extends AbstractFlow {

    @Override
    public void doExecute(ExecutionContext context) {
        //fetch command
        Command command = context.getCommand(AdditionCommand)

        //create command context
        CommandContext commandContext = new DefaultCommandContext()

        //set 'from' attribute
        commandContext.from(context.getFieldFromRequestBody(ScaffoldAdditionInput.OPERANDS))

        //set 'into' attribute
        commandContext.into("finalResult")

        //execute command
        command.execute(commandContext)

        //pass command output back to execution context and set response code
        context.setAttribute("finalResult", commandContext.getAttribute(commandContext.into()))
        context.setResponseCode(commandContext.getResponseCode())
    }
}
