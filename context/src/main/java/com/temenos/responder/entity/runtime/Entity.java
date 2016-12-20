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
    private final Map<String, Object> properties;
    private static final String PROPERTY_NOT_FOUND_MSG = "Property %s doesn't exist";
    private static final String TYPE_ERROR_MSG = "Expected %s but found %s";
    private static final String ARRAY_BOUNDS_EXCEEDED = "Tried to access element %d in a %d element array";

    public Entity(){
        Map<String, Object> initialiserMap = new HashMap<>();
        initialiserMap.put("_links", new ArrayList<>());
        initialiserMap.put("_embedded", new ArrayList<>());
        properties = initialiserMap;
    }

    public Entity(Map<String, Object> properties){
        this.properties = new HashMap<>();
        this.properties.put("_links", new ArrayList<Object>());
        this.properties.put("_embedded", new ArrayList<Object>());
        this.properties.putAll(properties);
    }

    public Object get(String key) throws EntityException {
        return get(key, properties);
    }

    private Object get(String key, Object properties) throws EntityException {
        StringTokenizer tokenizer = new StringTokenizer(key, ".");
        Pattern pattern = Pattern.compile("(\\[(\\d*)+?\\])+?");
        Object current = properties;
        while(tokenizer.hasMoreTokens()){
            String segment = tokenizer.nextToken();
            //TODO: use a map to improve efficiency
            List<String> matches = findArrayAccessExpression(segment, pattern);
            if(!matches.isEmpty()){
                segment = segment.substring(0, segment.indexOf('['));
                Object expectedList = ((Map<?, ?>)current).get(segment);
                if(!(expectedList instanceof List)){
                    throw new TypeMismatchException(String.format(TYPE_ERROR_MSG, "an array", "a map"));
                }
                List<?> listElement = (List<?>)expectedList;
                for(int index = 0, requestedIndex = 0; index < matches.size() - 1; index++){
                    requestedIndex = Integer.parseInt(matches.get(index));
                    listElement = (List<?>) listElement.get(requestedIndex);
                }
                int bounds = listElement.size(), access = Integer.parseInt(matches.get(matches.size() - 1));
                if(access >= bounds){
                    throw new IndexOutOfBoundsException(String.format(ARRAY_BOUNDS_EXCEEDED, access + 1, bounds));
                }else{
                    current = listElement.get(access);
                }
            }else{
                if(current instanceof List){
                    throw new TypeMismatchException(String.format(TYPE_ERROR_MSG, "a map", "an array"));
                }else if(!(current instanceof Map)){
                    throw new PropertyNotFoundException(String.format(PROPERTY_NOT_FOUND_MSG, key));
                }
                current = ((Map<?, ?>)current).get(segment);
            }
        }
        return current;
    }

    private List<String> findArrayAccessExpression(String token, Pattern pattern){
        List<String> matches = new ArrayList<>();
        Matcher matcher = pattern.matcher(token);
        while(matcher.find()){
            matches.add(matcher.group(2));
        }
        return matches;
    }

    public List<String> getEntityNames(){
        return getEntityNames(properties);
    }

    private List<String> getEntityNames(Object properties){
        return null;
    }

}
