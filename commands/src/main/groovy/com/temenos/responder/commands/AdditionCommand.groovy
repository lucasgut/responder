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
        int sum = 0
        commandContext.from().each { element ->
            sum += element
        }
        commandContext.setResponseCode(Response.Status.OK.statusCode as String)
        commandContext.setAttribute(commandContext.into(), new Entity([(ScaffoldAdditionOutput.RESULT):sum]))
    }

}
