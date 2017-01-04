package com.temenos.responder.validator

import com.temenos.responder.scaffold.Scaffold
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.entity.runtime.Type

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class ModelValidator implements Validator {

    @Override
    boolean isValid(Entity entity, Class<Scaffold> scaffold) {
        if(entity == null && scaffold == null){
            return true
        }
        def entityNames = entity.getEntityNames()
        def entityNamesAndTypes = entity.getEntityNamesAndTypes()
        def result = true
        scaffold.getEnumConstants().each { constant ->
            if(!entityNames.contains(constant.getName()) || !entityNamesAndTypes.get(constant.getName()).equals(constant.getType())){
                //TODO: throw exception?
                result = false
                return false
            }
        }
        return result
    }

    @Override
    boolean isValid(Entity entity, Entity model) {
        Map<String, Type> entityNamesAndTypes = entity.getEntityNamesAndTypes()
        for(Map.Entry<String, Type> entry : entityNamesAndTypes) {
            String modelType = model.getType(entry.getKey())
            String modelName = model.get(entry.getKey())
            boolean equalTypes = modelType != entry.getValue()
            boolean equalObjects = modelName != entry.getValue()
            if(!equalTypes || !equalObjects)
                return false
        }
        return true;
    }
}
