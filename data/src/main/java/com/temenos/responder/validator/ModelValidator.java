package com.temenos.responder.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.examples.Utils;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.temenos.responder.entity.exception.PropertyNotFoundException;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.entity.runtime.Type;
import com.temenos.responder.scaffold.Scaffold;
import groovy.json.JsonBuilder;

import java.io.IOException;
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
                    //validate an array by looking at the first item
                    Object field = entity.get(((String)scaffold.getDeclaredField(f.getName().substring(0, f.getName().indexOf("_TYPE"))).get(null)).replace("%d", "0"));
                    Type fieldType = entity.getType(((String)scaffold.getDeclaredField(f.getName().substring(0, f.getName().indexOf("_TYPE"))).get(null)).replace("%d", "0"));
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

    @Override
    public boolean isValid(Entity entity, String jsonSchema) {
        ObjectMapper mapper = new ObjectMapper();
        String entityString = new JsonBuilder(entity.getValues()).toString();

        boolean isValid = false;

        try {
            JsonNode entityNode = mapper.readValue(entityString, JsonNode.class);
            JsonNode schemaNode = mapper.readValue(jsonSchema, JsonNode.class);

            final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
            JsonSchema schema = factory.getJsonSchema(schemaNode);

            final ProcessingReport report = schema.validate(entityNode);
            isValid = report.isSuccess();

        } catch (ProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isValid;
    }
}
