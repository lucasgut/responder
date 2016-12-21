package com.temenos.responder.entity.runtime;

import java.util.List;
import java.util.Map;

/**
 * Created by Douglas Groves on 20/12/2016.
 */
public enum Type implements IType {
    INTEGER("integer", Integer.class),
    NUMBER("number", Number.class),
    STRING("string", String.class),
    NULL("null", Void.class),
    BOOLEAN("boolean", Boolean.class),
    OBJECT("object", Map.class),
    ARRAY("array", List.class);

    final String type;
    final Class<?> staticType;

    Type(final String type, final Class<?> staticType){
        this.type = type;
        this.staticType = staticType;
    }

    public String getType() {
        return type;
    }

    public Class<?> getStaticType() {
        return staticType;
    }

    public static Type fromStaticType(Class<?> staticType){
        for(Type type : Type.values()){
            if(type.staticType.isAssignableFrom(staticType)){
                return type;
            }
        }
        return null;
    }
}