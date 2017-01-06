package com.temenos.responder.flows

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.Command
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext

/**
 * Created by Douglas Groves on 05/01/2017.
 */
abstract class AbstractFlow implements Flow {

    @Override
    public final void execute(ExecutionContext context){
        doExecute(context)
        generateSelfLink(context)
    }

    public abstract void doExecute(ExecutionContext context);

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