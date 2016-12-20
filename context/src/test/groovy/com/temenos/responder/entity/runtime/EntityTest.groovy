package com.temenos.responder.entity.runtime

import com.temenos.responder.entity.exception.PropertyNotFoundException
import com.temenos.responder.entity.exception.TypeMismatchException
import spock.lang.Specification
import spock.lang.Unroll

import java.util.regex.Matcher
import java.util.regex.Pattern

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
            'Greeting.Subject.LastName'       | ['Greeting': ['Subject': ['FirstName': 'Jim', 'LastName': 'Smith']]]                                        | 'Smith'
            'Greeting.Subject'                | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                                             | ['Village', 'Town', 'City', 'World']
            'Greeting.Subject[0]'             | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                                             | 'Village'
            'Greeting.Subject[1]'             | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                                             | 'Town'
            'Greeting.Subject[0].LastName'    | ["Greeting": ["Subject": [['LastName': 'Village'], [['LastName': 'Town']]]]]                                | 'Village'
            'Subject[0][1]'                   | ["Subject": [["Alpha", "Beta", "Gamma"], ["1", "2", "3"], ["A", "B", "C"]]]                                 | 'Beta'
            'Greeting.Subject[1][1]'          | ["Greeting": ["Subject": [["Alpha", "Beta", "Gamma"], ["1", "2", "3"], ["A", "B", "C"]]]]                   | '2'
            'Greeting.Subject[2][2]'          | ["Greeting": ["Subject": [["Alpha", "Beta", "Gamma"], ["1", "2", "3"], ["A", "B", "C"]]]]                   | 'C'
            'Greeting.Subject[0][1].LastName' | ["Greeting": ["Subject": [[["LastName": "Ferrari"], ["LastName": "Lamborghini"], ["LastName": "Escort"]]]]] | "Lamborghini"
    }

    @Unroll
    def "Getter throws #exception.simpleName with message: \'#message\' if #cause"(key, map, exception, message, cause) {
        setup:
            def entity = new Entity(map)
        when:
            def result = entity.get(key)
        then:
            def expectedException = thrown(exception)
            expectedException.message == message
        where:
            key                        | map                                                             | exception                 | message                                          | cause
            'Greeting.Missing'         | ["Greeting": "Hello World!"]                                    | PropertyNotFoundException | 'Property Greeting.Missing doesn\'t exist'       | 'a matching property doesn\'t exist'
            'Greeting[0]'              | ["Greeting": "Hello World!"]                                    | TypeMismatchException     | 'Expected an array but found a map'              | 'an invalid type specifier is given'
            'Greeting.Subject.Missing' | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]] | TypeMismatchException     | 'Expected a map but found an array'              | 'an invalid type specifier is given'
            'Greeting.Subject[4]'      | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]] | IndexOutOfBoundsException | 'Tried to access element 5 in a 4 element array' | 'array bounds are exceeded'
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
            "getEntityNames"         | ['Greeting', 'Subject']                           | ['Greeting': 'Hello', 'Subject': 'World']
            "getEntityNames"         | ['Greeting', 'Greeting.Subject']                  | ['Greeting': ['Subject': 'World']]
            "getEntityNamesAndTypes" | ['Greeting': Type.STRING, 'Subject': Type.STRING] | ['Greeting': 'Hello', 'Subject': 'World']
    }
}
