package com.temenos.responder.producer

import com.temenos.responder.entity.runtime.Document
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 05/01/2017.
 */
class DocumentJsonProducerTest extends Specification {
    @Unroll
    def "Serialise #data as #output"(data, links, embedded, output) {
        given:
            def producer = new DocumentJsonProducer()
            def document = new Document(new Entity(links), new Entity(embedded), new Entity(data), 'hello')
        when:
            def result = producer.serialise(document)
        then:
            result == output
        where:
            data                                      | links                                      | embedded | output
            ["Greeting": "Hello", "Subject": 'World'] | ['self': ['href': 'http://0.0.0.0/hello']] | [:]      | '{"_links":{"self":{"href":"http://0.0.0.0/hello"}},"_embedded":{},"hello":{"Greeting":"Hello","Subject":"World"}}'
    }

}
