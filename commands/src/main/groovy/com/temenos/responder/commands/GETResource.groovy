package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.producer.EntityJsonProducer
import com.temenos.responder.producer.EntityProducer
import com.temenos.responder.producer.EntityXMLProducer
import groovy.json.JsonSlurper
import groovyx.net.http.HTTPBuilder

/**
 * Request an external resource over HTTP and store the result into the {@link CommandContext context attribute}.
 *
 * @author Douglas Groves
 */
class GETResource implements Command {

    private final HTTPBuilder http
    private final Map<String, EntityProducer> producers

    GETResource() {
        this.http = new HTTPBuilder()
        this.producers = ['application/json': new EntityJsonProducer(), 'application/xml': new EntityXMLProducer()]
    }

    GETResource(HTTPBuilder http, Map<String, EntityProducer> producers) {
        this.http = http
        this.producers = producers
    }

    @Override
    void execute(CommandContext context) {
        List<String> params = context.from()
        def requestParam = [:]
        String output = context.into()
        requestParam['uri'] = params[0]
        if (params[1] && !params[1].isEmpty())
            requestParam['queryString'] = params[1]
        InputStream result = (InputStream) http.get(requestParam)
        Object response = params[2] && producers[params[2]] ? producers[params[2]].deserialise(result.getText()) : result.getText()
        context.setAttribute(output, response)
    }
}
