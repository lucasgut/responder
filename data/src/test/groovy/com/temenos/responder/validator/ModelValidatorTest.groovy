package com.temenos.responder.validator

import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.Scaffold
import com.temenos.responder.scaffold.ScaffoldComplexCustomer
import com.temenos.responder.scaffold.ScaffoldCustomer
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 06/01/2017.
 */
class ModelValidatorTest extends Specification {

    @Unroll
    def "ModelValidator #action #data as #condition"(value, action, data, condition, Class scaffold) {
        given:
            def validator = new ModelValidator()
            Entity entity = new Entity(data)
        when:
            def result = validator.isValid(entity, scaffold)
        then:
            result == value
        where:
            value | action        | data                                                                                                                           | condition                                                      | scaffold
            true  | 'validates'   | ['CustomerId': 12345, 'CustomerName': 'John Smith', 'CustomerAddress': 'Penny Lane']                                           | 'it is valid'                                                  | ScaffoldCustomer
            false | 'invalidates' | ['CustomerId': 12345]                                                                                                          | 'it is missing required fields'                                | ScaffoldCustomer
            false | 'invalidates' | ['CustomerId': 'abcd', 'CustomerName': 'John Smith', 'CustomerAddress': 'Penny Lane']                                          | 'it contains a field whose type is invalid'                    | ScaffoldCustomer
            true  | 'validates'   | [] as Map<String, Object>                                                                                                      | 'empty entity'                                                 | ScaffoldCustomer
            true  | 'validates'   | ['CustomerName': 'John Smith', 'Addresses': [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]] | 'it is valid'                                                  | ScaffoldComplexCustomer
//            false | 'invalidates' | ['CustomerId': 12345, 'Addresses': [["HouseNumber": 1, "Road": "Station Road"], ["Road": "Dustbin Road"]]]                     | 'it contains an array element that is missing required fields' | ScaffoldComplexCustomer
    }

    @Ignore
    def 'ModelValidator invalidates ["CustomerId": 12345, "Addresses": [["HouseNumber": 1, "Road": "Station Road"], ["Road": "Dustbin Road"]]] as it it contains an array element that is missing required fields'(){}

    def "Validator returns true if both entity and scaffold are null"() {
        given:
            def validator = new ModelValidator()
        when:
            def result = validator.isValid(null as Entity, null as Class<Scaffold>)
        then:
            result
    }

    @Ignore
    def 'ModelValidator validates an entity against an entity'(){}

}
