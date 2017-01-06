package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import groovyx.net.http.HTTPBuilder

/**
 * Created by Douglas Groves on 03/01/2017.
 */
class GETResource implements Command {

    private final HTTPBuilder http;

    GETResource(){
        this.http = new HTTPBuilder()
    }

    GETResource(HTTPBuilder http){
        this.http = http
    }

    @Override
    void execute(CommandContext context) {
        List<String> params = context.from()
        String output = context.into()
        def result = http.get(uri: params[0], queryString: params[1]) as Map
        context.setAttribute(output, new Entity(result))
    }
}
