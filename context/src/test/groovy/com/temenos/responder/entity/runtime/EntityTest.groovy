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
    def "Getter obtains value #expectedValue mapped to #key"(key, map, expectedValue) {
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
    }

    @Unroll
    def "Type-aware getter obtains #expectedValue as a #expectedType mapped to #key"(key, map, expectedValue, expectedType) {
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
            'Greeting'         | ["Greeting": "Hello World!"] | List.class   | TypeMismatchException     | 'Expected: List but found: String'         | 'an incorrect type qualifier has been used'
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
    @Ignore
    def "Setter sets #key to #value and alters map structure #originalStructure to #newStructure"(key, value, originalStructure, newStructure) {
        setup:
            def entity = new Entity(originalStructure)
        when:
            entity.set(key, value)
        then:
            entity.getValues() == newStructure
            entity.get(key) == value
        where:
            key                | value   | originalStructure     | newStructure
            'Greeting'         | 'Hello' | [:]                   | ["Greeting": "Hello"]
            'Greeting.Subject' | 'Hello' | ['Greeting': 'Hello'] | ["Greeting": ["Subject": "Hello"]]

    }

}
