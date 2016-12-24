package com.temenos.responder.commands

import com.temenos.responder.context.DefaultExecutionContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException

import javax.ws.rs.core.Response

/**
 * Created by aburgos on 23/12/2016.
 */
class CustomerInformation implements Command {

    @Override
    def execute(ExecutionContext executionContext) {
        //TODO: parameters are currently hardcoded as we are not using JSON for workflows yet
        try {
            def fromDirective = ['id']
            def intoDirective = 'finalResult'

            // get T24 customer information
            Command getT24Customer = new T24CustomerInformation()
            ExecutionContext t24ExecutionContext = new DefaultExecutionContext(executionContext.getSelf())
            t24ExecutionContext.setAttribute('id', executionContext.getAttribute(fromDirective[0]))
            getT24Customer.execute(t24ExecutionContext)
            Entity t24Customer = t24ExecutionContext.getAttribute('finalResult')
            // check result
            if(!t24ExecutionContext.getResponseCode().equals(Response.Status.OK.statusCode as String)) {
                executionContext.setResponseCode(t24ExecutionContext.getResponseCode() as String)
                executionContext.setAttribute(intoDirective, new Entity())
                return;
            }
            // transform T24 customer model into customer model
            Map<String, String> map = new HashMap<>()
            map.put("CustomerId", t24Customer.get("CUSTOMER_ID"))
            map.put("CustomerName", t24Customer.get("CUSTOMER_NAME"))
            map.put("CustomerAddress", t24Customer.get("CUSTOMER_ADDRESS"))

            Entity responseBody = new Entity(map);

            executionContext.setResponseCode(Response.Status.OK.statusCode as String)
            executionContext.setAttribute(intoDirective, responseBody)
        } catch(IOException exception) {
            executionContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}