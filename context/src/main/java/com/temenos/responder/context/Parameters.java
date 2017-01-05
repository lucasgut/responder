package com.temenos.responder.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by aburgos on 23/12/2016.
 */
public class Parameters {
    Map<String, String> parameters;

    public Parameters() {
        parameters = new HashMap<>();
    }

    public void setValue(String key, String value) {
        parameters.put(key, value);
    }

    public String getValue(String key) {
        return parameters.get(key);
    }

    public Set<String> getParameterKeys() { return parameters.keySet(); }
}
