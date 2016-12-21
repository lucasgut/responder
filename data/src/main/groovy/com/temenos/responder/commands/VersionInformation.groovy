package com.temenos.responder.commands

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.producer.JsonProducer

import javax.ws.rs.core.Response

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class VersionInformation implements Command {
    def execute(ExecutionContext executionContext){
        //TODO: parameters are currently hardcoded as we are not using JSON for workflows yet
        try {
            def fromDirective = ['versionInfo.json']
            def intoDirective = 'finalResult'
            def fileContents = executionContext.getScriptLoader().load(fromDirective.first())
            Entity deserialisedContents = executionContext.getProducer().deserialise(fileContents)
            executionContext.setResponseCode(Response.Status.OK.statusCode as String)
            executionContext.setAttribute(intoDirective, deserialisedContents)
        }catch(IOException exception){
            executionContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}
