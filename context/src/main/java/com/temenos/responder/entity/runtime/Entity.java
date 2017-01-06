package com.temenos.responder.entity.runtime;

import com.temenos.responder.entity.exception.EntityException;
import com.temenos.responder.entity.exception.PropertyNotFoundException;
import com.temenos.responder.entity.exception.TypeMismatchException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Douglas Groves on 16/12/2016.
 */
public class Entity {
    private final Map<String, Object> values;
    private final Map<String, Object> properties;
    private final Map<String, Type> fqPropertyNameAndType;
    private static final String PROPERTY_NOT_FOUND_MSG = "Property %s doesn't exist";

    public Entity(){
        values = new LinkedHashMap<>();
        properties = new LinkedHashMap<>();
        fqPropertyNameAndType = new LinkedHashMap<>();
    }

    public Entity(Map<String, Object> properties){
        this.values = new LinkedHashMap<>(properties);
        this.properties = new LinkedHashMap<>();
        this.fqPropertyNameAndType = new LinkedHashMap<>();
        getEntityNamesAndTypes(properties, "");
    }

    public Object get(String key) throws EntityException {
        if(!properties.containsKey(key)){
            throw new PropertyNotFoundException(String.format(PROPERTY_NOT_FOUND_MSG, key));
        }
        return properties.get(key);
    }

    public Type getType(String key) { return fqPropertyNameAndType.get(key); }

    public Set<String> getEntityNames(){
        return properties.keySet();
    }

    public Map<String, Type> getEntityNamesAndTypes(){
        return fqPropertyNameAndType;
    }

    public Map<String, Object> getProperties(){
        return this.properties;
    }

    public void set(String name, Object properties) {
        getEntityNamesAndTypes(properties, name);
    }

    private void getEntityNamesAndTypes(Object properties, String baseKey){
        if(properties instanceof Map){
            if(!baseKey.isEmpty()) {
                this.properties.put(baseKey, properties);
                fqPropertyNameAndType.put(baseKey, Type.fromStaticType(properties.getClass()));
            }
            for(Map.Entry<?, ?> entry : ((Map<?,?>)properties).entrySet()){
                getEntityNamesAndTypes(entry.getValue(), baseKey.isEmpty() ? (String)entry.getKey() : baseKey +"."+entry.getKey());
            }
        }else if(properties instanceof List){
            this.properties.put(baseKey, properties);
            fqPropertyNameAndType.put(baseKey, Type.fromStaticType(properties.getClass()));
            int index = 0;
            for(Object o : (List<?>)properties){
                getEntityNamesAndTypes(o, baseKey+"["+index+"]");
                index++;
            }
        }else{
            if(!baseKey.isEmpty()) {
                this.properties.put(baseKey, properties);
                fqPropertyNameAndType.put(baseKey, Type.fromStaticType(properties.getClass()));
            }
        }
    }

    public Map<String, Object> getValues(){
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(properties, entity.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(properties);
    }

}
