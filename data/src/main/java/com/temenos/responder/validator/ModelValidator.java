package com.temenos.responder.validator;

import com.temenos.responder.entity.exception.PropertyNotFoundException;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.entity.runtime.Type;
import com.temenos.responder.scaffold.Scaffold;

import java.lang.reflect.Field;

/**
 *
 *
 * @author Douglas Groves
 * @author Andres Burgos
 */
public class ModelValidator implements Validator {

    @Override
    public boolean isValid(Entity entity, Class<Scaffold> scaffold){
        if(entity == null && scaffold == null || entity.getEntityNames().isEmpty()) {
            return true;
        }
        try {
            for (Field f : scaffold.getDeclaredFields()) {
                if(f.getName().endsWith("_TYPE")){
                    Object field = entity.get((String)scaffold.getDeclaredField(f.getName().substring(0, f.getName().indexOf("_TYPE"))).get(null));
                    Type fieldType = entity.getType((String)scaffold.getDeclaredField(f.getName().substring(0, f.getName().indexOf("_TYPE"))).get(null));
                    if(field == null || fieldType != (Type)f.get(null)){
                        return false;
                    }
                }
            }
        }catch(IllegalAccessException|NoSuchFieldException e){
            throw new RuntimeException(e);
        }catch(PropertyNotFoundException pnfe){
            return false;
        }
        return true;
    }

    @Override
    public boolean isValid(Entity entity, Entity model) {
        return false;
    }
}
