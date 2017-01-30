package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity

import javax.ws.rs.core.Response

/**
 * Created by dgroves on 20/01/2017.
 */
class Embed implements Command {

    @Override
    void execute(CommandContext context) {
        def from = context.from() as List
        def into = context.into() as String
        def id = from[0], body = context.getAttribute(from[1])
        def model = ((Entity)body).getValues();
        context.setAttribute(into, new Entity([(id):model]))
        context.setResponseCode(Response.Status.OK.getStatusCode() as String)
    }
}
