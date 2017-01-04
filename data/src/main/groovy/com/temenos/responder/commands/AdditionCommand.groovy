package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 * Add two numbers together
 *
 * Created by Douglas Groves on 21/12/2016.
 */
class AdditionCommand implements Command {

    @Override
    def execute(CommandContext commandContext) {
        try {
            def from = commandContext.getAttribute("from")
            def into = commandContext.getAttribute("into")

            Integer sum = 0;
            for(Integer operand : from) {
                sum += operand
            }

            Map<String, Integer> map = new HashMap<>()
            //map.put("operands", operands)
            map.put("result", sum)
            Entity responseBody = new Entity(map);
            commandContext.setResponseCode(Response.Status.OK.statusCode as String)
            commandContext.setAttribute(into, responseBody)
        } catch(IOException exception) {
            commandContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            commandContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }

}
