package com.temenos.responder.validator

import com.sun.jndi.toolkit.url.Uri
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

    def 'ModelValidator validates an entity against a json schema'(value, entityData, jsonSchema) {
        given:
            def validator = new ModelValidator()
            def entity = new Entity(entityData)
        when:
            def result = validator.isValid(entity, jsonSchema)
        then:
            result == value
        where:
            value | entityData                             | jsonSchema
            true  | [] as Map                              | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\"}"
            true  | [] as Map                              | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}}}"
            false | ['customerName': '']                   | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}}}"
            true  | ['customerName': 'John Smith']         | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}}}"
            false | ['customerName': 'Barnaby Marmaduke Aloysius Benjy Cobweb Dartagnan Egbert Felix Gaspar Humbert Ignatius Jayden Kasper Leroy Maximilian Neddy Obiajulu Pepin Quilliam Rosencrantz Sexton Teddy Upwood Vivatma Wayland Xylon Yardley Zachary Usansk']    | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}}}"
            true  | ['customerName': 'John Smith', 'postCode': '0q0#0$0']  | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}}}"
            true  | ['customerName': 'John Smith', 'postCode': 'SE1 1BD']  | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}, \"postCode\": {\"type\": \"string\", \"pattern\": \"^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)\$\"}}}"
            false | ['customerName': 'John Smith', 'postCode': 'SE1_1BD']  | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}, \"postCode\": {\"type\": \"string\", \"pattern\": \"^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)\$\"}}}"
            true  | ['customerName': 'John Smith', 'postCode': '0q0#0$0']  | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"definitions\": {\"UkPostCode\": {\"type\": \"string\", \"pattern\": \"^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)\$\"}}, \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}}}"
            true  | ['customerName': 'John Smith', 'postCode': 'SE1 1BD']  | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"definitions\": {\"UkPostCode\": {\"type\": \"string\", \"pattern\": \"^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)\$\"}}, \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}, \"postCode\": {\"\$ref\": \"#/definitions/UkPostCode\"}}}"
            false | ['customerName': 'John Smith', 'postCode': 'SE1_1BD']  | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"definitions\": {\"UkPostCode\": {\"type\": \"string\", \"pattern\": \"^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)\$\"}}, \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}, \"postCode\": {\"\$ref\": \"#/definitions/UkPostCode\"}}}"
            true  | ['customerName': 'John Smith', 'postCode': 'SE1 1BD', 'relationship': 'sister']  | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"definitions\": {\"UkPostCode\": {\"type\": \"string\", \"pattern\": \"^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)\$\"}}, \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}, \"postCode\": {\"\$ref\": \"#/definitions/UkPostCode\"}}}"
            false | ['customerName': 'John Smith', 'postCode': 'SE1 1BD', 'relationship': 'sister']  | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"definitions\": {\"UkPostCode\": {\"type\": \"string\", \"pattern\": \"^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)\$\"}, \"relationship\": {\"oneOf\": [{\"enum\": [\"sibling\"], \"title\": \"Sibling\"}]}}, \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}, \"postCode\": {\"\$ref\": \"#/definitions/UkPostCode\"}, \"relationship\": {\"\$ref\": \"#/definitions/relationship\"}}}"
            true  | ['customerName': 'John Smith', 'postCode': 'SE1 1BD', 'relationship': 'sibling'] | "{\"\$schema\": \"http://www.rimdsl.org/contract/model/2.0/schema#\", \"type\": \"object\", \"definitions\": {\"UkPostCode\": {\"type\": \"string\", \"pattern\": \"^([A-PR-UWYZ0-9][A-HK-Y0-9][AEHMNPRTVXY0-9]?[ABEHMNPRVWXY0-9]? {1,2}[0-9][ABD-HJLN-UW-Z]{2}|GIR 0AA)\$\"}, \"relationship\": {\"oneOf\": [{\"enum\": [\"sibling\"], \"title\": \"Sibling\"}]}}, \"properties\": {\"customerName\": {\"type\": \"string\", \"minLength\": 1, \"maxLength\": 50}, \"postCode\": {\"\$ref\": \"#/definitions/UkPostCode\"}, \"relationship\": {\"\$ref\": \"#/definitions/relationship\"}}}"
    }
}
