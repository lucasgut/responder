package com.temenos.responder.context;

/**
 * A Context contains name-value pairings of attributes used by its caller.
 *
 * Created by Douglas Groves on 13/12/2016.
 */
public interface Context {
    /**
     * Retrieve a value from the Context map using its name.
     * @param name The key to which the value is mapped.
     * @return The object stored against the key.
     */
    Object getAttribute(String name);

    /**
     * Set a value in the context map.
     * @param name The name of the key.
     * @param value The value that will be mapped to the given key.
     * @return A boolean value indicating whether the operation was successful or not.
     */
    boolean setAttribute(String name, Object value);
}
