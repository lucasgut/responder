package com.temenos.responder.validator

import com.temenos.responder.commands.ScaffoldVersion
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 20/12/2016.
 */
class ModelValidatorTest extends Specification {
    def "Validator returns true if the given entity matches its model definition"() {
        given:
            def validator = new ModelValidator()
            Entity entity = Mock(Entity)
            1 * entity.getEntityNames() >> ["versionNumber", "buildDate", "blameThisPerson"]
        when:
            def result = validator.isValid(entity, ScaffoldVersion)
        then:
            result
    }

    @Unroll
    def "Validator returns false if property '#property' is missing"(property, entityNames) {
        given:
            def validator = new ModelValidator()
            Entity entity = Mock(Entity)
            1 * entity.getEntityNames() >> entityNames
        when:
            def result = validator.isValid(entity, ScaffoldVersion)
        then:
            !result
        where:
            property        | entityNames
            'versionNumber' | ['buildDate', 'blameThisPerson']
    }

    def "Validator returns false if property types are invalid"(){

    }

}
