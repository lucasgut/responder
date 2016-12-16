package com.temenos.responder.commands

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.producer.JsonProducer

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class VersionInformation implements Command {
    def execute(ExecutionContext executionContext){
        if(!parametersAreValid(executionContext)){
            return
        }
        try {
            def fromDirective = executionContext.getAttribute('from')
            def intoDirective = executionContext.getAttribute('into')
            def fileContents = executionContext.getScriptLoader().load(fromDirective.first())
            def deserialisedContents = executionContext.getProducer().deserialise(fileContents)
            executionContext.setAttribute(intoDirective, deserialisedContents)
        }catch(IOException exception){
            executionContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }

    //TODO: breaks single responsibility; should be handled elsewhere
    private boolean parametersAreValid(executionContext){
        if(!(executionContext.getAttribute('from') &&
                executionContext.getAttribute('from') instanceof List<?> &&
                executionContext.getAttribute('into'))){
            executionContext.setAttribute('exception', new ScriptExecutionException('Expected script attributes are missing'))
            return false
        }else {
            return true
        }
    }
}
