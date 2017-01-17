package com.temenos.responder.producer

import com.temenos.responder.entity.runtime.Entity
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by dgroves on 15/01/2017.
 */
class EntityXMLProducerTest extends Specification {

    @Unroll
    def "Deserialise a document containing #summary"(summary, data, output) {
        given:
            def producer = new EntityXMLProducer()
        when:
            def result = producer.deserialise(data)
        then:
            result.values == output
        where:
            summary                                                                                                          | data                                                                                                                                                                                                                                                                                                                                        | output
            '2 elements'                                                                                                     | '<root><Greeting>Hello</Greeting><Subject>World</Subject></root>'                                                                                                                                                                                                                                                                           | ["root": ["Greeting": "Hello", "Subject": 'World']]
            '2 elements with a tag containing one attribute'                                                                 | '<root><Greeting value="Guten Tag">Hello</Greeting><Subject value="Everyone">World</Subject></root>'                                                                                                                                                                                                                                        | ["root": ["Greeting": "Hello", "Greeting.value": "Guten Tag", "Subject": "World", "Subject.value": "Everyone"]]
            'a nested element'                                                                                               | '<root><Name>John Smith</Name><Addresses><Address>1 Bedrock</Address></Addresses></root>'                                                                                                                                                                                                                                                   | ["root": ["Name": "John Smith", "Addresses": ["Address": "1 Bedrock"]]]
            '2 nested elements with identical tag names'                                                                     | '<root><Name>John Smith</Name><Addresses><Address>1 Bedrock</Address><Address>1 Evergreen Terrace</Address></Addresses></root>'                                                                                                                                                                                                             | ["root": ["Name": "John Smith", "Addresses": [["Address": "1 Bedrock"], ["Address": "1 Evergreen Terrace"]]]]
            '2 nested elements with identical tag names and attributes'                                                      | '<root><Name>John Smith</Name><Addresses description="Places of residence"><Address>1 Bedrock</Address><Address>1 Evergreen Terrace</Address></Addresses></root>'                                                                                                                                                                           | ["root": ["Name": "John Smith", "Addresses": [["Address": "1 Bedrock"], ["Address": "1 Evergreen Terrace"]], "Addresses.description": "Places of residence"]]
            '1 element with an attribute and 2 nested elements with identical tag names'                                     | '<root><Name value="Someone Else">John Smith</Name><Addresses><Address>1 Bedrock</Address><Address>1 Evergreen Terrace</Address></Addresses></root>'                                                                                                                                                                                        | ["root": ["Name": "John Smith", "Name.value": "Someone Else", "Addresses": [["Address": "1 Bedrock"], ["Address": "1 Evergreen Terrace"]]]]
            '2 nested elements with identical tag names and another nested element with a different tag name'                | '<root><Name value="Someone Else">John Smith</Name><Addresses><Address>1 Bedrock</Address><Address>1 Evergreen Terrace</Address><Prospect>1 West Wallaby Street</Prospect></Addresses></root>'                                                                                                                                              | ["root": ["Name": "John Smith", "Name.value": "Someone Else", "Addresses": [["Address": "1 Bedrock"], ["Address": "1 Evergreen Terrace"], ["Prospect": "1 West Wallaby Street"]]]]
            '2 nested elements with identical tag names and another nested element with a different tag name and attributes' | '<root><Name value="Someone Else">John Smith</Name><Addresses><Address>1 Bedrock</Address><Address>1 Evergreen Terrace</Address><Prospect interested="yes">1 West Wallaby Street</Prospect></Addresses></root>'                                                                                                                             | ["root": ["Name": "John Smith", "Name.value": "Someone Else", "Addresses": [["Address": "1 Bedrock"], ["Address": "1 Evergreen Terrace"], ["Prospect": "1 West Wallaby Street", "Prospect.interested": "yes"]]]]
            '2 level of nested elements'                                                                                     | '<root><Name>John Smith</Name><Addresses><Address><FirstLine>1 Bedrock</FirstLine><SecondLine>Bedrock County</SecondLine></Address></Addresses></root>'                                                                                                                                                                                     | ["root": ["Name": "John Smith", "Addresses": ["Address": ["FirstLine": "1 Bedrock", "SecondLine": "Bedrock County"]]]]
            '2 elements each containing 3 nested elements with identical tag names'                                          | '<root><Colours><Colour><Name>Red</Name><Red>255</Red><Green>0</Green><Blue>0</Blue></Colour><Colour><Name>Green</Name><Red>0</Red><Green>255</Green><Blue>0</Blue></Colour><Colour><Name>Blue</Name><Red>0</Red><Green>0</Green><Blue>255</Blue></Colour></Colours><Inks><Ink>Cyan</Ink><Ink>Magenta</Ink><Ink>Yellow</Ink></Inks></root>' | ["root": ['Colours': [['Colour': ['Name': 'Red', 'Red': '255', 'Green': '0', 'Blue': '0']], ['Colour': ['Name': 'Green', 'Red': '0', 'Green': '255', 'Blue': '0']], ['Colour': ['Name': 'Blue', 'Red': '0', 'Green': '0', 'Blue': '255']]], 'Inks': [['Ink': 'Cyan'], ['Ink': 'Magenta'], ['Ink': 'Yellow']]]]
            '3 elements with identical tag names'                                                                            | '<root><Colour><Name>Red</Name><Red>255</Red><Green>0</Green><Blue>0</Blue></Colour><Colour><Name>Green</Name><Red>0</Red><Green>255</Green><Blue>0</Blue></Colour><Colour><Name>Blue</Name><Red>0</Red><Green>0</Green><Blue>255</Blue></Colour></root>'                                                                                   | ["root": [['Colour': ['Name': 'Red', 'Red': '255', 'Green': '0', 'Blue': '0']], ['Colour': ['Name': 'Green', 'Red': '0', 'Green': '255', 'Blue': '0']], ['Colour': ['Name': 'Blue', 'Red': '0', 'Green': '0', 'Blue': '255']]]]
            'a root element with one attribute and two child elements'                                                       | '<root model="greetingModel"><Greeting>Hello</Greeting><Subject>World</Subject></root>'                                                                                                                                                                                                                                                     | ["root": ["Greeting": "Hello", "Subject": 'World'], "root.model": "greetingModel"]
    }

    @Ignore
    @Unroll
    def "Serialise #data as #output"(data, output) {
        given:
            def producer = new EntityXMLProducer()
            def entity = new Entity(data)
        when:
            def result = producer.serialise(entity)
        then:
            result == output
        where:
            data                                      | output
            ["Greeting": "Hello", "Subject": 'World'] | '<root><Greeting>Hello</Greeting><Subject>World</Subject></root>'
    }

}
