package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.scaffold.ScaffoldAdditionOutput
import com.temenos.responder.scaffold.ScaffoldVersion

import javax.ws.rs.core.Response

/**
 * Sum a {@link java.util.List list} of numbers together and return the result of the operation.
 *
 * @author Douglas Groves
 */
class AdditionCommand implements Command {

    @Override
    void execute(CommandContext commandContext) {
        int sum = 0
        commandContext.from().each { element ->
            sum += element
        }
        commandContext.setResponseCode(Response.Status.OK.statusCode)
        commandContext.setAttribute(commandContext.into(), new Entity([(ScaffoldAdditionOutput.RESULT):sum]))
    }

}
