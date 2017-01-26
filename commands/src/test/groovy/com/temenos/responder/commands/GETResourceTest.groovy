package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import groovyx.net.http.HTTPBuilder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 03/01/2017.
 */
class GETResourceTest extends Specification {

    @Unroll
    def "Fetch data from '#uri?#queryParams' and set context attribute 'finalResult' to returned data"(uri, queryParams, data) {
        given:
            def http = Spy(HTTPBuilder)
            def command = new GETResource(http)
            def ctx = Mock(CommandContext)
        when:
            def result = command.execute(ctx)
        then:
            1 * ctx.from() >> [uri, queryParams]
            1 * ctx.into() >> "finalResult"
            1 * ctx.setAttribute("finalResult", new Entity(data))
            1 * http.get(_) >> data
        where:
            uri                      | queryParams | data
            'http://0.0.0.0/example' | 'a=1&b=2'   | ["Greeting": "Hello", "Subject": "World"]
    }
}
