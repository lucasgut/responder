package com.temenos.responder.flows

import com.temenos.responder.commands.Command
import com.temenos.responder.commands.VersionInformation
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext

/**
 * This flow reads a generated JSON file and forwards its contents to the response.
 *
 * Created by aburgos on 16/01/2017.
 */
class VersionInformation2Flow extends AbstractFlow {

    @Override
    public void doExecute(ExecutionContext context) {
        //fetch command
        Command command = context.getCommand(VersionInformation.class)

        //create command-scoped context
        CommandContext ctx = new DefaultCommandContext()

        //set 'from' and 'into' attributes
        ctx.from(["versionInfo2.json"])
        ctx.into("finalResult")

        //execute the command
        command.execute(ctx)

        //pass command output and response codes back to execution context
        context.setAttribute("finalResult", ctx.getAttribute("finalResult"))
        context.setResponseCode(ctx.getResponseCode())
    }

}
