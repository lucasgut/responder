package com.temenos.responder.validator

import com.temenos.responder.scaffold.Scaffold
import com.temenos.responder.scaffold.ScaffoldVersion
import com.temenos.responder.entity.runtime.Type
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
            1 * entity.getEntityNamesAndTypes() >> ["versionNumber": Type.STRING, "buildDate": Type.STRING, "blameThisPerson": Type.STRING]
        when:
            def result = validator.isValid(entity, ScaffoldVersion)
        then:
            result
    }

    @Unroll
    def "Validator returns false if property '#property' is missing"(property, entityNames, entityNamesAndTypes) {
        given:
            def validator = new ModelValidator()
            Entity entity = Mock(Entity)
            1 * entity.getEntityNames() >> entityNames
            1 * entity.getEntityNamesAndTypes() >> entityNamesAndTypes
        when:
            def result = validator.isValid(entity, ScaffoldVersion)
        then:
            !result
        where:
            property        | entityNames                      | entityNamesAndTypes
            'versionNumber' | ['buildDate', 'blameThisPerson'] | ["versionNumber": Type.STRING, "buildDate": Type.STRING, "blameThisPerson": Type.STRING]
    }

    def "Validator returns false if property types are invalid"() {
        given:
            def validator = new ModelValidator()
            Entity entity = Mock(Entity)
            1 * entity.getEntityNames() >> ["versionNumber", "buildDate", "blameThisPerson"]
            1 * entity.getEntityNamesAndTypes() >> ["versionNumber": Type.NUMBER, "buildDate": Type.STRING, "blameThisPerson": Type.STRING]
        when:
            def result = validator.isValid(entity, ScaffoldVersion)
        then:
            !result
    }

    def "Validator returns true if both entity and scaffold are null"(){
        given:
            def validator = new ModelValidator()
        when:
            def result = validator.isValid(null as Entity, null as Class<Scaffold>)
        then:
            result
    }

}
