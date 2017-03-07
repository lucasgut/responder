package com.temenos.responder.entity.runtime

import com.temenos.responder.entity.exception.PropertyNotFoundException
import com.temenos.responder.entity.exception.TypeMismatchException
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 16/12/2016.
 */
class EntityTest extends Specification {
    @Unroll
    def "Access #expectedValue from entity #map using #key"(key, map, expectedValue) {
        setup:
            def entity = new Entity(map);
        when:
            def result = entity.get(key);
        then:
            result == expectedValue
        where:
            key                               | map                                                                                                         | expectedValue
            'Greeting'                        | ['Greeting': 'Hello World!']                                                                                | 'Hello World!'
            'Greeting'                        | ['Subject': 'World', 'Greeting': 'Hello']                                                                   | 'Hello'
            'Greeting.Subject'                | ['Greeting': ['Subject': 'World']]                                                                          | 'World'
            'Greeting.Subject'                | ['Greeting': ['Subject': ['FirstName': 'Jim', 'LastName': 'Smith']]]                                        | ['FirstName': 'Jim', 'LastName': 'Smith']
            'Greeting.Subject.LastName'       | ['Greeting': ['Subject': ['FirstName': 'Jim', 'LastName': 'Smith']]]                                        | 'Smith'
            'Greeting.Subject'                | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                                             | ['Village', 'Town', 'City', 'World']
            'Greeting.Subject[0]'             | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                                             | 'Village'
            'Greeting.Subject[1]'             | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                                             | 'Town'
            'Greeting.Subject[0].LastName'    | ["Greeting": ["Subject": [['LastName': 'Village'], [['LastName': 'Town']]]]]                                | 'Village'
            'Subject[0][1]'                   | ["Subject": [["Alpha", "Beta", "Gamma"], ["1", "2", "3"], ["A", "B", "C"]]]                                 | 'Beta'
            'Greeting.Subject[1][1]'          | ["Greeting": ["Subject": [["Alpha", "Beta", "Gamma"], ["1", "2", "3"], ["A", "B", "C"]]]]                   | '2'
            'Greeting.Subject[2][2]'          | ["Greeting": ["Subject": [["Alpha", "Beta", "Gamma"], ["1", "2", "3"], ["A", "B", "C"]]]]                   | 'C'
            'Greeting.Subject[0][1].LastName' | ["Greeting": ["Subject": [[["LastName": "Ferrari"], ["LastName": "Lamborghini"], ["LastName": "Escort"]]]]] | "Lamborghini"
            'Greeting.Subject'                | ["Greeting": ["Subject": "Everybody"], "Greeting.Subject": "World"]                                         | "Everybody"
            'Greeting\\.Subject'              | ["Greeting": ["Subject": "Everybody"], "Greeting.Subject": "World"]                                         | "World"
            'Greeting'                        | ['Greeting': null]                                                                                          | null
    }

    @Unroll
    def "Access #expectedValue from entity #map using #key and declared type #expectedType"(key, map, expectedValue, expectedType) {
        setup:
            def entity = new Entity(map)
        when:
            def result = entity.get(key, expectedType)
        then:
            result == expectedValue
            expectedType.isAssignableFrom(result.getClass())
        where:
            key                               | map                                                                                                         | expectedValue                             | expectedType
            'Greeting'                        | ['Greeting': 'Hello World!']                                                                                | 'Hello World!'                            | String.class
            'Greeting'                        | ['Subject': 'World', 'Greeting': 'Hello']                                                                   | 'Hello'                                   | String.class
            'Greeting.Subject'                | ['Greeting': ['Subject': 'World']]                                                                          | 'World'                                   | String.class
            'Greeting.Subject'                | ['Greeting': ['Subject': ['FirstName': 'Jim', 'LastName': 'Smith']]]                                        | ['FirstName': 'Jim', 'LastName': 'Smith'] | Map.class
            'Greeting.Subject.LastName'       | ['Greeting': ['Subject': ['FirstName': 'Jim', 'LastName': 'Smith']]]                                        | 'Smith'                                   | String.class
            'Greeting.Subject'                | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                                             | ['Village', 'Town', 'City', 'World']      | List.class
            'Greeting.Subject[0]'             | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                                             | 'Village'                                 | String.class
            'Greeting.Subject[1]'             | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                                             | 'Town'                                    | String.class
            'Greeting.Subject[0].LastName'    | ["Greeting": ["Subject": [['LastName': 'Village'], [['LastName': 'Town']]]]]                                | 'Village'                                 | String.class
            'Subject[0][1]'                   | ["Subject": [["Alpha", "Beta", "Gamma"], ["1", "2", "3"], ["A", "B", "C"]]]                                 | 'Beta'                                    | String.class
            'Greeting.Subject[1][1]'          | ["Greeting": ["Subject": [["Alpha", "Beta", "Gamma"], ["1", "2", "3"], ["A", "B", "C"]]]]                   | '2'                                       | String.class
            'Greeting.Subject[2][2]'          | ["Greeting": ["Subject": [["Alpha", "Beta", "Gamma"], ["1", "2", "3"], ["A", "B", "C"]]]]                   | 'C'                                       | String.class
            'Greeting.Subject[0][1].LastName' | ["Greeting": ["Subject": [[["LastName": "Ferrari"], ["LastName": "Lamborghini"], ["LastName": "Escort"]]]]] | "Lamborghini"                             | String.class
            'Greeting.Subject'                | ["Greeting": ["Subject": "Everybody"], "Greeting.Subject": "World"]                                         | "Everybody"                               | String.class
            'Greeting\\.Subject'              | ["Greeting": ["Subject": "Everybody"], "Greeting.Subject": "World"]                                         | "World"                                   | String.class
    }

    @Unroll
    def "Access null value from entity #map using #key and declared type Void.class"(key, map) {
        given:
            def entity = new Entity(map)
        when:
            def result = entity.get(key, Void.class)
        then:
            result == null
        where:
            key                   | map
            'Greeting'            | ['Greeting': null]
            'Greeting.Subject'    | ['Greeting': ['Subject': null]]
            'Greeting[0]'         | ['Greeting': [null, null, null]]
            'Greeting[0].Subject' | ['Greeting': [['Subject': null]]]

    }

    @Unroll
    def "Getter throws #exception.simpleName with message: \'#message\' if #cause"(key, map, exception, message, cause) {
        setup:
            def entity = new Entity(map)
        when:
            entity.get(key)
        then:
            def expectedException = thrown(exception)
            expectedException.message == message
        where:
            key                | map                          | exception                 | message                                    | cause
            'Greeting'         | [] as Map                    | PropertyNotFoundException | 'Property Greeting doesn\'t exist'         | 'a matching property doesn\'t exist'
            'Greeting.Missing' | ["Greeting": "Hello World!"] | PropertyNotFoundException | 'Property Greeting.Missing doesn\'t exist' | 'a matching property doesn\'t exist'
    }

    @Unroll
    def "Type-aware getter throws #exception.simpleName with message: \'#message\' if #cause"(key, map, expectedType, exception, message, cause) {
        setup:
            def entity = new Entity(map)
        when:
            def result = entity.get(key, expectedType)
        then:
            def expectedException = thrown(exception)
            expectedException.message == message
        where:
            key                | map                          | expectedType | exception                 | message                                    | cause
            'Greeting.Missing' | ["Greeting": "Hello World!"] | String.class | PropertyNotFoundException | 'Property Greeting.Missing doesn\'t exist' | 'a matching property doesn\'t exist'
            'Greeting'         | ["Greeting": "Hello World!"] | List.class   | TypeMismatchException     | 'Expected: List but found: String'         | 'an incorrect type term has been used'
    }

    @Unroll
    def "Entity.#getterMethod returns #entityNames from map #map"(getterMethod, entityNames, map) {
        setup:
            def entity = new Entity(map)
        when:
            def result = entity."${getterMethod}"()
        then:
            result == entityNames
        where:
            getterMethod             | entityNames                                       | map
            "getEntityNames"         | ['Greeting', 'Subject'] as Set                    | ['Greeting': 'Hello', 'Subject': 'World']
            "getEntityNames"         | ['Greeting', 'Greeting.Subject'] as Set           | ['Greeting': ['Subject': 'World']]
            "getEntityNamesAndTypes" | ['Greeting': Type.STRING, 'Subject': Type.STRING] | ['Greeting': 'Hello', 'Subject': 'World']
    }

    @Unroll
    def "Setter sets #key to #value and alters map structure #originalStructure to #newStructure"(key, value, originalStructure, newStructure) {
        setup:
            def entity = new Entity(originalStructure)
        when:
            entity.set(key, value)
        then:
            entity.getValues() == newStructure
            entity.get(key) == value
        where:
            key                             | value          | originalStructure                                                                                                                                                                                                                                                                 | newStructure
            'Greeting'                      | 'Hello'        | [:]                                                                                                                                                                                                                                                                               | ["Greeting": "Hello"]
            'Greeting.Subject'              | 'World'        | [:]                                                                                                                                                                                                                                                                               | ["Greeting": ["Subject": "World"]]
            'Greeting[0]'                   | 'World'        | [:]                                                                                                                                                                                                                                                                               | ["Greeting": ["World"]]
            'Greeting'                      | 'Ciao'         | ['Greeting': 'Hello']                                                                                                                                                                                                                                                             | ["Greeting": "Ciao"]
            'Subject'                       | 'World'        | ['Greeting': 'Hello']                                                                                                                                                                                                                                                             | ["Greeting": "Hello", "Subject": "World"]
            'Greeting\\.Subject'            | 'Everybody'    | ["Greeting": ["Greeting": "Hello", "Subject": "World"]]                                                                                                                                                                                                                           | ["Greeting": ["Greeting": "Hello", "Subject": "World"], "Greeting.Subject": "Everybody"]
            'Greeting\\.Subject'            | 'Everybody'    | ['Greeting.Subject': 'World']                                                                                                                                                                                                                                                     | ['Greeting.Subject': 'Everybody']
            'Greeting.Subject'              | 'Everybody'    | ["Greeting": ["Greeting": "Hello", "Subject": "World"]]                                                                                                                                                                                                                           | ["Greeting": ["Greeting": "Hello", "Subject": "Everybody"]]
            'Greeting.Subject.Location'     | 'World'        | ["Greeting": ["Subject": ["Location": "Everywhere"]]]                                                                                                                                                                                                                             | ["Greeting": ["Subject": ["Location": "World"]]]
            'Greeting[0]'                   | 'Ciao'         | ["Greeting": ["Hello", "Bonjour", "Guten Tag"]]                                                                                                                                                                                                                                   | ["Greeting": ["Ciao", "Bonjour", "Guten Tag"]]
            'Greeting[1]'                   | 'Ciao'         | ["Greeting": ["Hello"]]                                                                                                                                                                                                                                                           | ["Greeting": ["Hello", "Ciao"]]
            'Greeting[10]'                  | 'Hola'         | ["Greeting": ["Hello", "Hey", "Howdy", "Hi", "Good day", "Yo", "Good evening", "Guten Tag", "Bonjour", "Ciao"]]                                                                                                                                                                   | ["Greeting": ["Hello", "Hey", "Howdy", "Hi", "Good day", "Yo", "Good evening", "Guten Tag", "Bonjour", "Ciao", "Hola"]]
            'Greeting[10]'                  | 'Hej'          | ["Greeting": ["Hello", "Hey", "Howdy", "Hi", "Good day", "Yo", "Good evening", "Guten Tag", "Bonjour", "Ciao", "Hola"]]                                                                                                                                                           | ["Greeting": ["Hello", "Hey", "Howdy", "Hi", "Good day", "Yo", "Good evening", "Guten Tag", "Bonjour", "Ciao", "Hej"]]
            'Greeting[10].Greeting'         | 'Hej'          | ["Greeting": [["Greeting": "Hello"], ["Greeting": "Hey"], ["Greeting": "Howdy"], ["Greeting": "Hi"], ["Greeting": "Good day"], ["Greeting": "Yo"], ["Greeting": "Good evening"], ["Greeting": "Guten Tag"], ["Greeting": "Bonjour"], ["Greeting": "Ciao"]]]                       | ["Greeting": [["Greeting": "Hello"], ["Greeting": "Hey"], ["Greeting": "Howdy"], ["Greeting": "Hi"], ["Greeting": "Good day"], ["Greeting": "Yo"], ["Greeting": "Good evening"], ["Greeting": "Guten Tag"], ["Greeting": "Bonjour"], ["Greeting": "Ciao"], ["Greeting": "Hej"]]]
            'Greeting[10].Greeting'         | 'Hej'          | ["Greeting": [["Greeting": "Hello"], ["Greeting": "Hey"], ["Greeting": "Howdy"], ["Greeting": "Hi"], ["Greeting": "Good day"], ["Greeting": "Yo"], ["Greeting": "Good evening"], ["Greeting": "Guten Tag"], ["Greeting": "Bonjour"], ["Greeting": "Ciao"], ["Greeting": "Hola"]]] | ["Greeting": [["Greeting": "Hello"], ["Greeting": "Hey"], ["Greeting": "Howdy"], ["Greeting": "Hi"], ["Greeting": "Good day"], ["Greeting": "Yo"], ["Greeting": "Good evening"], ["Greeting": "Guten Tag"], ["Greeting": "Bonjour"], ["Greeting": "Ciao"], ["Greeting": "Hej"]]]
            'Greeting[0].Greeting'          | 'Ciao'         | ["Greeting": [["Greeting": "Hello"], ["Greeting": "Bonjour"], ["Greeting": "Guten Tag"]]]                                                                                                                                                                                         | ["Greeting": [["Greeting": "Ciao"], ["Greeting": "Bonjour"], ["Greeting": "Guten Tag"]]]
            'Greeting[0].Subject'           | 'Everyone'     | ["Greeting": [["Greeting": "Hello"], ["Greeting": "Bonjour"], ["Greeting": "Guten Tag"]]]                                                                                                                                                                                         | ["Greeting": [["Greeting": "Hello", "Subject": "Everyone"], ["Greeting": "Bonjour"], ["Greeting": "Guten Tag"]]]
            'Greeting[3].Subject'           | 'Everyone'     | ["Greeting": [["Greeting": "Hello"], ["Greeting": "Bonjour"], ["Greeting": "Guten Tag"]]]                                                                                                                                                                                         | ["Greeting": [["Greeting": "Hello"], ["Greeting": "Bonjour"], ["Greeting": "Guten Tag"], ["Subject": "Everyone"]]]
            'Greeting.Subject[0].Location'  | 'World'        | ["Greeting": ["Subject": [["Location": "Everywhere"]]]]                                                                                                                                                                                                                           | ["Greeting": ["Subject": [["Location": "World"]]]]
            'Address[0].Address[0].Address' | 'Station Road' | [:]                                                                                                                                                                                                                                                                               | ['Address': [['Address': [["Address": "Station Road"]]]]]
            'Address[0].Address[1].Address' | 'Station Road' | ["Address": [['Address': [['Address': 'Turing Road']]]]]                                                                                                                                                                                                                          | ['Address': [['Address': [["Address": "Turing Road"], ["Address": "Station Road"]]]]]
            'Address[0].Address[0].Address' | 'Station Road' | ["Address": [['Address': [['Address': 'Turing Road']]]]]                                                                                                                                                                                                                          | ['Address': [['Address': [["Address": "Station Road"]]]]]
            'Address'                       | null           | ["Address": 'Turing Road']                                                                                                                                                                                                                                                        | ["Address": null]
            'Greeting.Subject'              | null           | ["Greeting": ["Greeting": "Hello"]]                                                                                                                                                                                                                                               | ["Greeting": ["Greeting": "Hello", "Subject": null]]
            'Greeting[0]'                   | null           | ["Greeting": ["Hello", "Ciao", "Hola", "Bonjour"]]                                                                                                                                                                                                                                | ["Greeting": [null, "Ciao", "Hola", "Bonjour"]]
            'Greeting[1]'                   | null           | ["Greeting": ["Hello"]]                                                                                                                                                                                                                                                           | ["Greeting": ["Hello", null]]
    }

    @Unroll
    def "Convert #accessor to #data"(accessor, value, data) {
        when:
            def result = Entity.accessorToDataStructure(accessor, value)
        then:
            result == data
        where:
            accessor                        | value                             | data
            'Greeting'                      | 'Hello'                           | ["Greeting": 'Hello']
            'Greetings'                     | ['Hello', 'Bonjour', 'Guten Tag'] | ['Greetings': ['Hello', 'Bonjour', 'Guten Tag']]
            'Greetings[0]'                  | 'Hello'                           | ['Greetings': ['Hello']]
            'Greetings[1]'                  | 'Hello'                           | ['Greetings': ['Hello']]
            'Greetings[0][1]'               | 'Hello'                           | ['Greetings': [['Hello']]]
            'Greetings[0].Subject'          | 'Everybody'                       | ['Greetings': [['Subject': 'Everybody']]]
            'Greetings[0][1].Subject'       | 'Hello'                           | ['Greetings': [[['Subject': 'Hello']]]]
            'Greetings.Subject.Location'    | 'World'                           | ['Greetings': ['Subject': ['Location': 'World']]]
            'Address[0].Address[0].Address' | 'Station Road'                    | ['Address': [['Address': [["Address": "Station Road"]]]]]
    }

    @Unroll
    def "Tokenize #accessor as #data"(accessor, data) {
        when:
            def result = Entity.tokeniseAccessor(accessor)
        then:
            result == data
        where:
            accessor                                  | data
            'Greetings.Greeting'                      | ["Greeting", "Greetings"]
            'Greetings[0]'                            | ["[]", "Greetings"]
            'Greetings[0][1][1][1][1][1][1]'          | ["[][][][][][][]", "Greetings"]
            'Greetings[0].Greeting'                   | ["Greeting", "[]", "Greetings"]
            'Greetings[0].Greeting[0]'                | ["[]", "Greeting", "[]", "Greetings"]
            'Greetings[0][1][1][1][1][1][1].Greeting' | ["Greeting", "[][][][][][][]", 'Greetings']
            'Greetings\\.Greeting'                    | ['Greetings.Greeting']
            'Greetings\\.Greeting[0]'                 | ["[]", 'Greetings.Greeting']
            'Greetings\\.Greeting.Greeting'           | ['Greeting', 'Greetings.Greeting']
            'Greetings.Subject.Location'              | ['Location', 'Subject', 'Greetings']
            'Address[0].Address[0].Address'           | ['Address', '[]', 'Address', '[]', 'Address']
            'Address[10].Address[10].Address'         | ['Address', '[]', 'Address', '[]', 'Address']
    }

}
