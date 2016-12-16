package com.temenos.responder.producer

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class JsonProducerTest extends Specification {

    @Unroll
    def "Serialise #data as #output"(data, output) {
        when:
            def result = new JsonProducer().serialise(data)
        then:
            result == output
        where:
            data                                      | output
            ["Greeting": "Hello", "Subject": 'World'] | '{"Greeting":"Hello","Subject":"World"}'
    }

    @Unroll
    def "Deserialise #data as #output"(data, output) {
        when:
            def result = new JsonProducer().deserialise(data)
        then:
            result == output
        where:
            data                                     | output
            '{"Greeting":"Hello","Subject":"World"}' | ["Greeting": "Hello", "Subject": 'World']
    }
}
