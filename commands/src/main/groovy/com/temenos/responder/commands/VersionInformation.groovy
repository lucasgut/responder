package com.temenos.responder.commands

import com.google.inject.Inject
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.loader.ScriptLoader
import com.temenos.responder.producer.Producer

import javax.ws.rs.core.Response

/**
 * Inject the contents of a generated JSON file back into the command context for use by the calling
 * {@link com.temenos.responder.flows.Flow flow}. If an error occurs while loading the file, the response code will be
 * HTTP 500 Internal Server Error and exception details will be added to the
 * {@link com.temenos.responder.context.CommandContext command context}.
 *
 * @author Douglas Groves
 */
class VersionInformation implements Command {

    private final ScriptLoader loader
    private final Producer producer

    @Inject
    VersionInformation(ScriptLoader loader, Producer producer){
        this.loader = loader
        this.producer = producer
    }

    @Override
    void execute(CommandContext commandContext){
        try {
            List<String> fromDirective = commandContext.from()
            String intoDirective = commandContext.into()
            String fileContents = loader.load(fromDirective[0])
            Entity deserialisedContents = (Entity)producer.deserialise(fileContents)
            commandContext.setResponseCode(Response.Status.OK.statusCode as String)
            commandContext.setAttribute(intoDirective, deserialisedContents)
        }catch(IOException exception){
            commandContext.setResponseCode(Response.Status.INTERNAL_SERVER_ERROR.statusCode as String)
            commandContext.setAttribute('exception', new ScriptExecutionException(exception))
        }
    }
}
