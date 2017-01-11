package com.temenos.responder.context;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to store a map of resolved path variables.
 *
 * @author Andres Burgos
 */
public class Parameters {
    private final Map<String, String> parameters;

    public Parameters() {
        parameters = new HashMap<>();
    }

    /**
     * Set a parameter.
     *
     * @param key The name of the parameter.
     * @param value The value of the parameter that will be mapped against the given name.
     */
    public void setValue(String key, String value) {
        parameters.put(key, value);
    }

    /**
     * Obtain a parameter value using its mapped name.
     * @param key The name of the parameter.
     * @return The value of the parameter mapped against the given name or null if a mapping does not exist.
     */
    public String getValue(String key) {
        return parameters.get(key);
    }

    /**
     * Obtain a list of all parameter names.
     *
     * @return A list of all parameter names.
     */
    public Set<String> getParameterKeys() {
        return parameters.keySet();
    }
}
