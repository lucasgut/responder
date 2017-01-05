package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import groovyx.net.http.ContentType
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.Method

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
