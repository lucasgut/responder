package com.temenos.responder.producer

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.temenos.responder.configuration.ResourceSpec
import spock.lang.Specification

/**
 * Created by aburgos on 13/01/2017.
 */
class JsonNodeProducerTest extends Specification {
    def "Deserialise #input as #output"(input, output) {
        given:
            def producer = new JsonNodeProducer()
        when:
            def result = producer.deserialise(input)
        then:
            result.fieldNames().collect().forEach { k ->
                assert output[k] == result.get(k).asText()
            }
        where:
            input                                    | output
            '{"Greeting":"Hello","Subject":"World"}' | ["Greeting": "Hello", "Subject": 'World']
    }

    def "Serialise #data"(data) {
        given:
            def producer = new JsonNodeProducer()
            def jsonNode = producer.deserialise(data)
        when:
            def result = producer.serialise(jsonNode)
        then:
            result == data
        where:
            data << ['{"Greeting":"Hello","Subject":"World"}']
    }
}
