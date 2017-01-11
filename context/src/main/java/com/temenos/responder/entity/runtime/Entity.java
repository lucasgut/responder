package com.temenos.responder.entity.runtime;

import com.temenos.responder.entity.exception.EntityException;
import com.temenos.responder.entity.exception.PropertyNotFoundException;
import com.temenos.responder.entity.exception.TypeMismatchException;

import java.util.*;

/**
 * An entity is a data structure whose elements are accessible using dot and/or bracket notation.
 *
 * @author Douglas Groves
 * @author Andres Burgos
 */
public class Entity {
    private final Map<String, Object> values;
    private final Map<String, Object> accessors;
    private final Map<String, Type> fqAccessorNameAndType;
    private static final String PROPERTY_NOT_FOUND_MSG = "Property %s doesn't exist";
    private static final String INVALID_TYPE_MSG = "Expected: %s but found: %s";

    public Entity() {
        values = new LinkedHashMap<>();
        accessors = new LinkedHashMap<>();
        fqAccessorNameAndType = new LinkedHashMap<>();
    }

    public Entity(Map<String, Object> values) {
        this.values = new LinkedHashMap<>(values);
        this.accessors = new LinkedHashMap<>();
        this.fqAccessorNameAndType = new LinkedHashMap<>();
        getEntityNamesAndTypes(values, "");
    }

    /**
     * Obtain an element from the entity instance using dot and/or bracket notation. Consider the following
     * data structure (written in JSON for simplicity):
     * <pre>
     * {
     *   "orchestra": {
     *       "name": "Chamber Trio",
     *       "instruments": [
     *        {
     *           "name": "Violin"
     *       },
     *       {
     *           "name": "Viola"
     *       },
     *       {
     *           "name: "Cello"
     *       }
     *       ],
     *       "attendence": [10,15,3]
     *   }
     * }
     * </pre>
     *
     * @param key An accessor. For the above example, the following accessors are supported:<br>
     *            <b>orchestra.name</b> returns <i>"Chamber Quartet"</i><br>
     *            <b>orchestra.instruments</b> returns <i>[["name":"Violin"],["name":"Viola"],["name":"Cello"]]</i><br>
     *            <b>orchestra.instruments[0]</b> returns <i>["name":"Violin"]</i><br>
     *            <b>orchestra.instruments[0].name</b> returns <i>"Violin"</i><br>
     * @return The object mapped to the given accessor name.
     * @throws EntityException If no matching field can be found for the given accessor.
     */
    public Object get(String key) throws EntityException {
        if (!accessors.containsKey(key)) {
            throw new PropertyNotFoundException(String.format(PROPERTY_NOT_FOUND_MSG, key));
        }
        return accessors.get(key);
    }

    /**
     * Obtain an element from the entity instance using dot and/or bracket notation and cast it to the given class.
     * Consider the following data structure (written in JSON for simplicity):
     * <pre>
     * {
     *   "orchestra": {
     *       "name": "Chamber Trio",
     *       "instruments": [
     *        {
     *           "name": "Violin"
     *       },
     *       {
     *           "name": "Viola"
     *       },
     *       {
     *           "name: "Cello"
     *       }
     *       ],
     *       "attendence": [10,15,3]
     *   }
     * }
     * </pre>
     *
     * @param key      An accessor. For the above example, the following accessors are supported:<br>
     *                 <b>orchestra.name</b> returns <i>"Chamber Quartet"</i><br>
     *                 <b>orchestra.instruments</b> returns <i>[["name":"Violin"],["name":"Viola"],["name":"Cello"]]</i><br>
     *                 <b>orchestra.instruments[0]</b> returns <i>["name":"Violin"]</i><br>
     *                 <b>orchestra.instruments[0].name</b> returns <i>"Violin"</i><br>
     * @param expected The expected type of the element.
     * @param <T>      The class to which the element will be cast.
     * @return An element cast to the expected type.
     * @throws EntityException If no matching field can be found for the given accessor or if there is a mismatch between
     *                         declared and actual types.
     */
    public <T> T get(String key, Class<T> expected) throws EntityException {
        Object value = get(key);
        Class<?> valueType = value.getClass();
        if (!expected.isAssignableFrom(valueType)) {
            throw new TypeMismatchException(String.format(INVALID_TYPE_MSG,
                    expected.getSimpleName(), valueType.getSimpleName()));
        } else {
            return expected.cast(value);
        }
    }

    /**
     * Update or add another field to the entity definition and regenerate the map of valid accessors.
     *
     * @param name       The name of the field being added or updated.
     * @param properties The [new] value that the field will be set to.
     */
    public void set(String name, Object properties) {
        values.put(name, properties);
        getEntityNamesAndTypes(properties, name);
    }

    /**
     * Obtain {@link Type type} information for a field accessed using dot and/or bracket notation. Consider the following
     * data structure (written in JSON for simplicity):
     * <pre>
     *     {
     *          "Name": "John",
     *          "Age": 30,
     *          "Height": 1.2,
     *          "Registered": true,
     *          "Hostnames": ["someISP", "anotherISP"],
     *          "Addresses": {
     *              "Primary": "Portcullis House",
     *              "Secondary": "Parliament Square"
     *          }
     *     }
     * </pre>
     *
     * @param key An accessor. For the above example, the following {@link Type types} will be returned:<br>
     *            <b>Name</b> returns {@link Type#STRING}<br>
     *            <b>Age</b> returns {@link Type#INTEGER}<br>
     *            <b>Height</b> returns {@link Type#NUMBER}<br>
     *            <b>Registered</b> returns {@link Type#BOOLEAN}<br>
     *            <b>Hostnames</b> returns {@link Type#ARRAY}<br>
     *            <b>Addresses</b> returns {@link Type#OBJECT}<br>
     *            <b>Addresses.Primary</b> returns {@link Type#STRING}<br>
     * @return The {@link Type data type} of the entity field being accessed.
     */
    public Type getType(String key) {
        return fqAccessorNameAndType.get(key);
    }

    public Set<String> getEntityNames() {
        return accessors.keySet();
    }

    public Map<String, Type> getEntityNamesAndTypes() {
        return fqAccessorNameAndType;
    }

    public Map<String, Object> getAccessors() {
        return accessors;
    }

    private void getEntityNamesAndTypes(Object properties, String baseKey) {
        if (properties instanceof Map) {
            if (!baseKey.isEmpty()) {
                accessors.put(baseKey, properties);
                fqAccessorNameAndType.put(baseKey, Type.fromStaticType(properties.getClass()));
            }
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) properties).entrySet()) {
                String key = ((String) entry.getKey()).replace(".", "\\.");
                getEntityNamesAndTypes(entry.getValue(), baseKey.isEmpty() ? key : baseKey + "." + key);
            }
        } else if (properties instanceof List) {
            accessors.put(baseKey, properties);
            fqAccessorNameAndType.put(baseKey, Type.fromStaticType(properties.getClass()));
            int index = 0;
            for (Object o : (List<?>) properties) {
                getEntityNamesAndTypes(o, baseKey + "[" + index + "]");
                index++;
            }
        } else {
            if (!baseKey.isEmpty()) {
                accessors.put(baseKey, properties);
                fqAccessorNameAndType.put(baseKey, Type.fromStaticType(properties.getClass()));
            }
        }
    }

    public Map<String, Object> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof Entity)) {
            return false;
        }
        Entity entity = (Entity) o;
        return Objects.equals(values, entity.values);
    }

    @Override
    public int hashCode() {
        return Objects.hash(values);
    }

}
