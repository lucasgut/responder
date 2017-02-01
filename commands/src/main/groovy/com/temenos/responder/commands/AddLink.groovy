package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity

import javax.ws.rs.core.Response

/**
 * Add a link to a given {@link com.temenos.responder.context.CommandContext command context} context attribute.
 *
 * @author Douglas Groves
 */
class AddLink implements Command {

    @Override
    void execute(CommandContext context) {
        def from = context.from() as List
        def into = context.into() as String
        def id = from[0], uri = from[1], name = from[2], description = from[3]
        def myMap = [:]
        myMap[id] = ['href':uri]
        if(name){
            myMap[id]['name'] = name
        }
        if(description){
            myMap[id]['description'] = description
        }
        def entity = new Entity(myMap)
        context.setAttribute(into, entity)
        context.setResponseCode(Response.Status.OK.getStatusCode())
    }
}
