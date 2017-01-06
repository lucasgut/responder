package com.temenos.responder.validator;

import com.temenos.responder.scaffold.Scaffold;
import com.temenos.responder.entity.runtime.Entity;

/**
 * Created by Douglas Groves on 18/12/2016.
 */
public interface Validator {
    boolean isValid(Entity entity, Class<Scaffold> scaffold);
    boolean isValid(Entity entity, Entity model);
}
