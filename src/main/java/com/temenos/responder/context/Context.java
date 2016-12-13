package com.temenos.responder.context;

/**
 * Created by Douglas Groves on 13/12/2016.
 */
public interface Context {
    Object getAttribute(String name);
    boolean setAttribute(String name, Object value);
}
