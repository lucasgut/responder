package com.temenos.responder.flows

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.Command
import com.temenos.responder.commands.VersionInformation
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.context.ExecutionContext

/**
 * This flow reads a generated JSON file and forwards its contents to the response.
 *
 * Created by Douglas Groves on 04/01/2017.
 */
class VersionInformationFlow implements Flow {

    @Override
    def execute(ExecutionContext context) {
        //fetch command
        Command command = context.getCommand(VersionInformation.class)

        //create command-scoped context
        CommandContext ctx = new DefaultCommandContext()

        //set 'from' attribute
        ctx.setAttribute("from", ["versionInfo.json"])

        //set 'into' attribute
        ctx.setAttribute("into", "finalResult")

        //execute the command
        command.execute(ctx)

        //pass command output and response codes back to execution context
        context.setAttribute("finalResult", ctx.getAttribute("finalResult"))
        context.setResponseCode(ctx.getResponseCode())

        //add self link
        generateSelfLink(context)
    }

    //TODO: move upwards to an abstract class and extend
    private void generateSelfLink(ExecutionContext context){
        //fetch command
        Command command = context.getCommand(AddLink.class);

        //create command-scoped context
        CommandContext ctx = new DefaultCommandContext()

        //set 'from' and 'into' attributes
        ctx.setAttribute("from", [context.getSelf(), context.getResourceName()])
        ctx.setAttribute("into", 'document.links.self')

        //execute the command
        command.execute(ctx)

        //pass command output back to execution context
        context.setAttribute('document.links.self', ctx.getAttribute('document.links.self'))
    }
}
