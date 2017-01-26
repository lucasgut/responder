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
    private Map<String, Object> values;
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

    private int lastIndexOfDisregardEscapedDots(String str){
        for(int index = str.length() - 1; index > 0; index--){
            if(str.charAt(index) == '.' && str.charAt(index - 1) != '\\'){
                return index;
            }
        }
        return -1;
    }

    /**
     * Update or add another field to the entity definition and regenerate the map of valid accessors.
     * <p>
     * TODO: Refactor this method and fix unchecked casts.
     *
     * @param name  An accessor referring to the field being added or updated.
     * @param value The [new] value that the field will be set to.
     */
    @SuppressWarnings("unchecked")
    public void set(String name, Object value) {
        //if an accessor already exists
        if (exists(name)) {
            int lastDotAccessor = lastIndexOfDisregardEscapedDots(name);
            int bracketAccessor = name.indexOf("[");
            //ascend to its container
            if (lastDotAccessor == -1 && bracketAccessor == -1) {
                this.values.put(name.replace("\\.","."), value);
            }else if(lastDotAccessor == -1){
                String arrayAccessor = name.substring(0, bracketAccessor);
                String arrayIndex = name.substring(bracketAccessor + 1, name.indexOf("]"));
                //replace existing value with the new value
                ((List<Object>) ((Map<?, ?>) accessors).get(arrayAccessor)).set(Integer.parseInt(arrayIndex), value);
            } else {
                String parentContainer = name.substring(0, lastIndexOfDisregardEscapedDots(name));
                String fieldName = name.substring(name.lastIndexOf(".") + 1, name.length());
                //replace existing value with the new value
                ((Map<String, Object>) accessors.get(parentContainer)).put(fieldName, value);
            }
        }
        //we are adding a new field; search accessors for the first available container using the given accessor
        else {
            Object container = null;
            String accessor = name, rebuildFrom = "";
            int lastDotAccessor = accessor.lastIndexOf("."), lastArrayAccessor = accessor.lastIndexOf("[");
            boolean rebuild = true;
            Map<String, Object> structure = new LinkedHashMap<>();
            //subtract segments from the accessor until a valid accessor is found
            while (container == null) {
                //it's an object
                if (lastDotAccessor != -1 && !accessor.endsWith("]")) {
                    rebuildFrom = accessor.substring(accessor.lastIndexOf(".") + 1, accessor.length())+(rebuildFrom.isEmpty() ? "" : "."+rebuildFrom);
                    accessor = accessor.substring(0, accessor.lastIndexOf("."));
                    container = ((Map<?, ?>) accessors).get(accessor);
                    if(container == null) {
                        structure.put(rebuildFrom, value);
                    }
                }
                //it's an array
                else if (lastArrayAccessor != -1) {
                    String expr = accessor;
                    accessor = accessor.substring(0, lastArrayAccessor);
                    container = ((Map<?, ?>) accessors).get(accessor);
                    //the container doesn't exist and there are no further accessors
                    if(container == null && rebuildFrom.isEmpty()){
                        rebuildFrom = expr;
                        break;
                    }
                    //the container doesn't exist
                    else if(container == null){
                        rebuildFrom = expr + "."+rebuildFrom;
                        break;
                    }
                    //the container exists so add the value
                    else{
                        List<Object> myContainer = (List<Object>)container;
                        if(structure.isEmpty()) {
                            myContainer.add(value);
                        }else{
                            myContainer.add(structure);
                        }
                        rebuild = false;
                        break;
                    }
                } else {
                    rebuildFrom = accessor + "." + rebuildFrom;
                    break;
                }
                lastDotAccessor = accessor.lastIndexOf(".");
                lastArrayAccessor = accessor.lastIndexOf("[");
            }
            //rebuild values using the parts of the accessor that were removed
            if(rebuild && container == null) {
                this.values.putAll((Map<String, Object>) Entity.accessorToDataStructure(rebuildFrom, value));
            }else if(rebuild && container instanceof Map){
                ((Map<String, Object>) container).putAll((Map<String,Object>)Entity.accessorToDataStructure(rebuildFrom, value));
            }else if(rebuild && container instanceof List){
                ((List<Object>) container).add(((Map<String,Object>)Entity.accessorToDataStructure(rebuildFrom, value)));
            }
        }
        getEntityNamesAndTypes(values, "");
    }

    public static Object accessorToDataStructure(String accessor, Object value) {
        Map<String,Object> result = new HashMap<>();
        Deque<String> accessorSegments = tokeniseAccessor(accessor);
        String parentField = accessorSegments.pollLast();
        while(!accessorSegments.isEmpty()){
            String nextAccessor = accessorSegments.poll();
            //it's an array
            if(nextAccessor.contains("[]")) {
                int arrayNesting = -1;
                while (!nextAccessor.isEmpty()) {
                    nextAccessor = nextAccessor.substring(0, nextAccessor.length() - 2);
                    arrayNesting++;
                }
                value = createNestedArray(new ArrayList<>(), value, arrayNesting);
            }
            //it's an object
            else{
                Map<String, Object> myMap = new LinkedHashMap<>();
                myMap.put(nextAccessor, value);
                value = myMap;
            }
        }
        result.put(parentField, value);
        return result;
    }

    public static Deque<String> tokeniseAccessor(String accessor){
        Deque<String> tokens = new LinkedList<>();
        Deque<String> arrayStack = new LinkedList<>();
        int index = 0;
        int finalIndex = 0;
        while(index < accessor.length()){
            char myChar = accessor.charAt(index);
            if(myChar == '.' && accessor.charAt(index - 1) != '\\'){
                if(!arrayStack.isEmpty()) {
                    tokens.push(arrayStack.pop());
                    finalIndex = index + 1;
                }else{
                    tokens.push(accessor.substring(finalIndex, index).replace("\\.","."));
                    finalIndex = index + 1;
                }
            }else if(myChar == '['){
                if(arrayStack.isEmpty()){
                    tokens.push(accessor.substring(finalIndex, index).replace("\\.","."));
                    arrayStack.push("[]");
                }else{
                    arrayStack.push(arrayStack.pop()+"[]");
                }
            }else if(myChar == ']'){
                finalIndex = index + 1;
            }
            index++;
        }
        if(finalIndex != index){
            tokens.push(accessor.substring(finalIndex, accessor.length()).replace("\\.","."));
        }
        if(!arrayStack.isEmpty()){
            tokens.push(arrayStack.pop());
        }
        return tokens;
    }

    private static List<Object> createNestedArray(List<Object> outer, Object value, int levels){
        if(levels == 0){
            outer.add(value);
            return outer;
        }else{
            outer.add(createNestedArray(new ArrayList<>(), value, --levels));
        }
        return outer;
    }

    private boolean exists(String accessor) {
        return accessors.get(accessor) != null;
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

    public Map<String, Object> getValues() {
        return values;
    }

    private void getEntityNamesAndTypes(Object properties, String baseKey) {
        if (properties instanceof Map) {
            if (!baseKey.isEmpty()) {
                accessors.put(baseKey, properties);
                fqAccessorNameAndType.put(baseKey, Type.fromStaticType(properties.getClass()));
            }
            Map<?, ?> propertiesMap = (Map<?, ?>) properties;
            if (!propertiesMap.isEmpty()) {
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) properties).entrySet()) {
                    String key = ((String) entry.getKey()).replace(".", "\\.");
                    getEntityNamesAndTypes(entry.getValue(), baseKey.isEmpty() ? key : baseKey + "." + key);
                }
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

    @Override
    public String toString() {
        return values.toString();
    }
}
