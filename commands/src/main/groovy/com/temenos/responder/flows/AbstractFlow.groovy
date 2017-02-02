package com.temenos.responder.flows

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.context.FlowResult

/**
 * An AbstractFlow executes a command and automatically adds a self link to the document.
 * Extend this class if you want to implement your own {@link com.temenos.responder.flow.Flow flow}.
 *
 * @author Douglas Groves
 */
abstract class AbstractFlow implements Flow {

    @Override
    public final FlowResult execute(ExecutionContext context){
        doExecute(context)
        generateSelfLink(context)
    }

    /**
     * Perform one or more actions contributing towards a defined workflow and finalise by inserting a self
     * link to the resource the flow was accessed with into the {@link ExecutionContext execution context} attribute
     * 'document.links.self'.
     *
     * @param context An {@link ExecutionContext execution context} instance. All flow parameters should be passed in using
     * {@link ExecutionContext#setAttribute(java.lang.String, java.lang.Object) the setAttribute method}.
     */
    public abstract FlowResult doExecute(ExecutionContext context);

    protected void generateSelfLink(ExecutionContext context){
        //fetch command
        Command command = context.getCommand(AddLink.class);

        //create command-scoped context
        CommandContext ctx = new DefaultCommandContext()

        //set 'from' and 'into' attributes
        ctx.from(['self', context.getSelf()])
        ctx.into('document.links.self')

        //execute the command
        command.execute(ctx)

        //pass command output back to execution context
        context.setAttribute('document.links.self', ctx.getAttribute('document.links.self'))
    }

}