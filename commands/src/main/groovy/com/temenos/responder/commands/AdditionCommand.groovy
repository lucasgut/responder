package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.scaffold.ScaffoldAdditionOutput
import com.temenos.responder.scaffold.ScaffoldVersion

import javax.ws.rs.core.Response

/**
 * Add a list of numbers together.
 *
 * Created by Douglas Groves on 21/12/2016.
 */
class AdditionCommand implements Command {

    @Override
    void execute(CommandContext commandContext) {
        try {
            def from = commandContext.getAttribute("from")
            def into = commandContext.getAttribute("into")
            int sum = 0;
            from.each { element ->
                sum += element
            }
            commandContext.setResponseCode(Response.Status.OK.statusCode as String)
            commandContext.setAttribute(into, new Entity([(ScaffoldAdditionOutput.RESULT):sum]))
        } catch(IOException exception) {
            commandContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            commandContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }

}
