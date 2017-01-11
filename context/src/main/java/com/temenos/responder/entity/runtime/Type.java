package com.temenos.responder.entity.runtime;

import java.util.List;
import java.util.Map;

/**
 * This class is used to attach or determine type information
 * to or from fields inside an {@link Entity entity}.
 *
 * @author Douglas Groves
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

    Type(final String type, final Class<?> staticType) {
        this.type = type;
        this.staticType = staticType;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public Class<?> getStaticType() {
        return staticType;
    }

    /**
     * Obtain a Type enumeration using a static {@link Class type}.
     *
     * @param staticType A declared type.
     * @return A Type enumeration corresponding to the
     * declared type that was used or null if a type cannot be determined.
     */
    public static Type fromStaticType(Class<?> staticType) {
        for (Type type : Type.values()) {
            if (type.staticType.isAssignableFrom(staticType)) {
                return type;
            }
        }
        return null;
    }
}