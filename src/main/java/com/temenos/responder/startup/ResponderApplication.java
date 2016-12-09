package com.temenos.responder.startup;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.ws.rs.core.Application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.temenos.responder.entity.configuration.Resource;
import com.temenos.responder.exception.ScriptExecutionException;
import com.temenos.responder.loader.ClasspathScriptLoader;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.scripting.GroovyScriptingEngine;
import com.temenos.responder.scripting.ScriptingEngine;
import com.temenos.responder.servlet.RequestHandler;

public class ResponderApplication extends Application {
    private Set<Object> singletons = new HashSet<Object>();
    private Set<Class<?>> classes = new HashSet<Class<?>>();
    private Map<String, Object> properties = new ConcurrentHashMap<String, Object>();

    public ResponderApplication() {
        //create a RequestHandler singleton for use across multiple requests
        properties.put("injector", Guice.createInjector(new ResponderBindings()));
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

    private void loadCoreResources() {
        ScriptLoader loader = new ClasspathScriptLoader();
        ScriptingEngine engine = new GroovyScriptingEngine(
                new ScriptEngineManager().getEngineByName("Groovy"), loader
        );
        try {
            Object resourceMap = engine.execute("com/temenos/responder/producer/JsonProducer",
                    "deserialise", loader.loadAll("resources/core"));
            List<?> resources = (List<?>) engine.execute(
                    "com/temenos/responder/mapper/ResourceMapper",
                    "mapAll", resourceMap
            );
            List<Resource> cleanResources = new ArrayList<>();
            for (Object o : resources) {
                cleanResources.add(Resource.class.cast(o));
            }
            properties.put("resources", cleanResources);
        } catch (IOException | ScriptExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
