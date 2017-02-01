package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.producer.EntityProducer
import groovyx.net.http.HTTPBuilder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 03/01/2017.
 */
class GETResourceTest extends Specification {

    @Unroll
    def "Fetch data from '#uri?#queryParams' and set context attribute 'finalResult' to #model"(uri, queryParams, contentType, expectedHttpParams, data, model) {
        given:
            def http = Spy(HTTPBuilder)
            def jsonProducer = Mock(EntityProducer), xmlProducer = Mock(EntityProducer)
            def producers = ['application/json': jsonProducer, 'application/xml': xmlProducer]
            def command = new GETResource(http, producers)
            def ctx = Mock(CommandContext)
            def fromDirective = [uri]
            fromDirective += queryParams
            if (contentType)
                fromDirective += contentType
        when:
            command.execute(ctx)
        then:
            1 * ctx.from() >> fromDirective
            1 * ctx.into() >> "finalResult"
            1 * ctx.setAttribute("finalResult", new Entity(model))
            1 * http.get(expectedHttpParams) >> new ByteArrayInputStream(data.getBytes())
            jsonProducer.deserialise(data) >> new Entity(model)
            xmlProducer.deserialise(data) >> new Entity(model)
        where:
            uri                          | queryParams | contentType        | expectedHttpParams                                              | data                                                              | model
            'http://0.0.0.0/jsonExample' | 'a=1&b=2'   | 'application/json' | ['uri': 'http://0.0.0.0/jsonExample', 'queryString': 'a=1&b=2'] | '{"Greeting": "Hello", "Subject": "World"}'                       | ['Greeting': 'Hello', 'Subject': 'World']
            'http://0.0.0.0/xmlExample'  | 'a=1&b=2'   | 'application/xml'  | ['uri': 'http://0.0.0.0/xmlExample', 'queryString': 'a=1&b=2']  | '<Data><Greeting>Hello</Greeting><Subject>World</Subject></Data>' | ['Data': ['Greeting': 'Hello', 'Subject': 'World']]
    }

    @Unroll
    def "Fetch data from '#uri?#queryParams' without a defined content type and set context attribute 'finalResult' to #data"(uri, queryParams, expectedHttpParams, data) {
        given:
            def http = Spy(HTTPBuilder)
            def jsonProducer = Mock(EntityProducer), xmlProducer = Mock(EntityProducer)
            def producers = ['application/json': jsonProducer, 'application/xml': xmlProducer]
            def command = new GETResource(http, producers)
            def ctx = Mock(CommandContext)
            def fromDirective = [uri]
            fromDirective += queryParams
        when:
            command.execute(ctx)
        then:
            1 * ctx.from() >> fromDirective
            1 * ctx.into() >> "finalResult"
            1 * ctx.setAttribute("finalResult", data)
            1 * http.get(expectedHttpParams) >> new ByteArrayInputStream(data.getBytes())
        where:
            uri                               | queryParams | expectedHttpParams                                                   | data
            'http://0.0.0.0/plaintextExample' | ''          | ['uri': 'http://0.0.0.0/plaintextExample']                           | '{"Greeting":"Hello","Subject":"world"}'
            'http://0.0.0.0/plaintextExample' | 'a=1&b=2'   | ['uri': 'http://0.0.0.0/plaintextExample', 'queryString': 'a=1&b=2'] | '{"Greeting":"Hello","Subject":"world"}'
    }
}
