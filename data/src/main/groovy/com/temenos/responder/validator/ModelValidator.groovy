package com.temenos.responder.validator

import com.temenos.responder.commands.Scaffold
import com.temenos.responder.entity.runtime.Entity

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class ModelValidator implements Validator {

    @Override
    boolean isValid(Entity entity, Class<Scaffold> scaffold) {
        def entityNames = entity.getEntityNames()
        def result = true
        scaffold.getEnumConstants().each { constant ->
            if(!entityNames.contains(constant.getName())){
                //TODO: throw exception?
                result = false
                return false
            }
        }
        return result
    }
}