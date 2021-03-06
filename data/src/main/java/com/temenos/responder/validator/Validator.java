package com.temenos.responder.validator;

import com.temenos.responder.scaffold.Scaffold;
import com.temenos.responder.entity.runtime.Entity;

import java.net.URI;

/**
 * @author Douglas Groves
 */
public interface Validator {
    boolean isValid(Entity entity, Class<Scaffold> scaffold);
    boolean isValid(Entity entity, Entity model);
    boolean isValid(Entity entity, String jsonSchema);
    boolean isValid(Entity entity, URI schemaURI);
}
