package com.temenos.responder.startup;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.ws.rs.core.Application;

import com.temenos.responder.controller.RequestHandler;

public class ResponderApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();
    private Map<String, Object> properties = new ConcurrentHashMap<String, Object>();

    public ResponderApplication(Map<String, Object> properties){
        classes.add(RequestHandler.class);
        this.properties = properties;
    }

    public ResponderApplication() {
        //construct RequestHandler objects per-request
        classes.add(RequestHandler.class);
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

}
