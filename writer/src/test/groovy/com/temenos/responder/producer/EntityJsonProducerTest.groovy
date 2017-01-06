package com.temenos.responder.producer

import com.temenos.responder.entity.runtime.Entity
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 20/12/2016.
 */
class EntityJsonProducerTest extends Specification {

    @Unroll
    def "Serialise #data as #output"(data, output) {
        given:
            def producer = new EntityJsonProducer()
            def entity = new Entity(data)
        when:
            def result = producer.serialise(entity)
        then:
            result == output
        where:
            data                                      | output
            ["Greeting": "Hello", "Subject": 'World'] | '{"Greeting":"Hello","Subject":"World"}'
    }

    @Unroll
    def "Deserialise #data as #output"(data, output) {
        given:
            def producer = new EntityJsonProducer()
        when:
            def result = producer.deserialise(data)
        then:
            result.properties == output
        where:
            data                                     | output
            '{"Greeting":"Hello","Subject":"World"}' | ["Greeting": "Hello", "Subject": 'World']
    }

}
