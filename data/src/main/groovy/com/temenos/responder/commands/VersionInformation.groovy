package com.temenos.responder.commands

import com.google.inject.Inject
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.loader.ClasspathScriptLoader
import com.temenos.responder.loader.ScriptLoader
import com.temenos.responder.producer.EntityJsonProducer
import com.temenos.responder.producer.JsonProducer
import com.temenos.responder.producer.Producer

import javax.ws.rs.core.Response

/**
 * This command injects the contents of a generated JSON file back into the command context for use by the calling
 * flow.
 *
 * If an error occurs while loading the file, the response code will be HTTP 500 Internal Server Error and an exception
 * will be passed back inside the command context.
 *
 * Created by Douglas Groves on 09/12/2016.
 */
class VersionInformation implements Command {

    private final ScriptLoader loader
    private final Producer producer

    @Inject
    VersionInformation(ScriptLoader loader, Producer producer){
        this.loader = loader
        this.producer = producer
    }

    void execute(CommandContext commandContext){
        try {
            def fromDirective = commandContext.getAttribute("from")
            def intoDirective = commandContext.getAttribute("into")
            def fileContents = loader.load(fromDirective.first())
            Entity deserialisedContents = (Entity)producer.deserialise(fileContents)
            commandContext.setResponseCode(Response.Status.OK.statusCode as String)
            commandContext.setAttribute(intoDirective, deserialisedContents)
        }catch(IOException exception){
            commandContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            commandContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}
