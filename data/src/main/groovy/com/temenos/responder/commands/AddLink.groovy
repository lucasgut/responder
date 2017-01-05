package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity

import javax.ws.rs.core.Response

/**
 * Created by Douglas Groves on 04/01/2017.
 */
class AddLink implements Command {

    @Override
    void execute(CommandContext context) {
        def from = context.getAttribute('from') as List
        def into = context.getAttribute('into') as String
        def uri = from[0], title = from[1], description = from[2]
        def myMap = [:]
        myMap[title] = ['href':uri,'name':title]
        if(description){
            myMap[title]['description'] = description
        }
        def entity = new Entity(myMap)
        context.setAttribute(into, entity)
        context.setResponseCode(Response.Status.OK.getStatusCode() as String)
    }
}
