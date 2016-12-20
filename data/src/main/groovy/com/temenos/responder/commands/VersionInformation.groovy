package com.temenos.responder.commands

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.producer.JsonProducer

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
            def deserialisedContents = [:]
            deserialisedContents['_links'] = ['self': ['href': executionContext.getSelf()]]
            deserialisedContents['_embedded'] = [:]
            deserialisedContents['core.VersionNumberModel'] = executionContext.getProducer().deserialise(fileContents)
            executionContext.setAttribute(intoDirective, deserialisedContents)
        }catch(IOException exception){
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}
