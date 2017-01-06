package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
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
        def params = context.getAttribute("from")
        def output = context.getAttribute("into")
        def result = http.get(uri: params[0], queryString: params[1])
        context.setAttribute(output, result)
    }
}
