package com.temenos.responder.entity.runtime

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
            key                            | map                                                                                     | expectedValue
            'Greeting'                     | ['Greeting': 'Hello World!']                                                            | 'Hello World!'
            'Greeting'                     | ['Subject': 'World', 'Greeting': 'Hello']                                               | 'Hello'
            'Greeting.Subject'             | ['Greeting': ['Subject': 'World']]                                                      | 'World'
            'Greeting.Subject.LastName'    | ['Greeting': ['Subject': ['FirstName': 'Jim', 'LastName': 'Smith']]]                    | 'Smith'
            'Greeting.Subject'             | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                         | ['Village', 'Town', 'City', 'World']
            'Greeting.Subject[0]'          | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                         | 'Village'
            'Greeting.Subject[1]'          | ["Greeting": ["Subject": ['Village', 'Town', 'City', 'World']]]                         | 'Town'
            'Greeting.Subject[0].LastName' | ["Greeting": [["Subject": ['LastName': 'Village']], ["Subject": ['LastName': 'Town']]]] | 'Village'
    }
}
