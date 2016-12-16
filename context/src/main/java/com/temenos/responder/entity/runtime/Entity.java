package com.temenos.responder.entity.runtime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Douglas Groves on 16/12/2016.
 */
public class Entity {
    private final Map<String, Object> properties;

    public Entity(){
        properties = new HashMap<>();
    }

    public Entity(Map<String, Object> properties){
        this.properties = new HashMap<>(properties);
    }

    public Object get(String key){
        return get(key, properties);
    }

    private Object get(String key, Map<String, Object> properties){
        int splitter = key.indexOf('.');
        String segment, keyMinusSegment;
        if(splitter != -1) {
            segment = key.substring(0, splitter);
            keyMinusSegment = key.replaceFirst(segment + ".", "");
        }else{
            segment = key;
            keyMinusSegment = null;
        }
        Pattern p = Pattern.compile("^.*?\\[(\\d*?)\\]$");
        Matcher m = p.matcher(segment);
        if(m.matches()){
            Integer index = Integer.parseInt(m.group(1));
            return ((List<?>)properties.get(segment.substring(0, segment.indexOf('[')))).get(index);
        }else if(splitter == -1){
            return properties.get(segment);
        }else{
            return get(keyMinusSegment, (Map<String,Object>)properties.get(segment));
        }
    }
}
