package com.temenos.responder.commands

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 * Created by Douglas Groves on 21/12/2016.
 */
class AdditionCommand implements Command {

    @Override
    def execute(ExecutionContext executionContext) {
        //TODO: parameters are currently hardcoded as we are not using JSON for workflows yet
        try {
            def fromDirective = []
            def intoDirective = 'finalResult'
            Entity requestBody = executionContext.getRequestBody()

            ArrayList<Integer> operands = requestBody.get("operands")
            Integer sum = 0;
            for(Integer operand : operands) {
                sum += operand
            }

            Map<String, Integer> map = new HashMap<>()
            //map.put("operands", operands)
            map.put("result", sum)
            Entity responseBody = new Entity(map);
            executionContext.setResponseCode(Response.Status.OK.statusCode as String)
            executionContext.setAttribute(intoDirective, responseBody)
        } catch(IOException exception) {
            executionContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }

}
