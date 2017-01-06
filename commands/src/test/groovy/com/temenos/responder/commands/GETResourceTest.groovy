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
    def "GET resource command fetches data from #uri"(uri, data) {
        given:
            def http = Spy(HTTPBuilder)
            def command = new GETResource(http)
            def ctx = Mock(CommandContext)
        when:
            def result = command.execute(ctx)
        then:
            1 * ctx.from() >> [uri, 'a=1&b=2']
            1 * ctx.into() >> "finalResult"
            1 * ctx.setAttribute("finalResult", new Entity(data))
            1 * http.get(_) >> data
        where:
            uri                      | data
            'http://0.0.0.0/example' | ["Greeting": "Hello", "Subject": "World"]
    }
}
