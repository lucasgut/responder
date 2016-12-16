package com.temenos.responder.startup;

import com.google.inject.*;
import com.temenos.responder.context.Context;
import com.temenos.responder.loader.ClasspathScriptLoader;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.paths.PathHandler;
import com.temenos.responder.paths.ResourcePathHandler;
import com.temenos.responder.producer.JsonProducer;
import com.temenos.responder.producer.Producer;
import com.temenos.responder.scripting.GroovyScriptingEngine;
import com.temenos.responder.scripting.ScriptingEngine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Douglas Groves on 15/12/2016.
 */
public class ApplicationContext extends AbstractModule implements Context {

    private final ScriptEngine G_SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("groovy");
    private final ScriptLoader CP_SCRIPT_LOADER = new ClasspathScriptLoader(RESOURCE_ROOT);
    private final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private static Injector INJECTOR_INSTANCE;
    private static final String RESOURCE_ROOT = "resources";

    private ApplicationContext(){}

    @Override
    protected void configure() {
        bind(ScriptLoader.class).toInstance(CP_SCRIPT_LOADER);
        bind(PathHandler.class).to(ResourcePathHandler.class);
        bind(ScriptingEngine.class).to(GroovyScriptingEngine.class);
        bind(Producer.class).to(JsonProducer.class);
        bind(ScriptEngine.class).toInstance(G_SCRIPT_ENGINE);
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public boolean setAttribute(String name, Object value) {
        attributes.put(name, value);
        return true;
    }

    public static <T> T getInjector(Class<T> clazz){
        if(INJECTOR_INSTANCE == null){
            INJECTOR_INSTANCE = Guice.createInjector(new ApplicationContext());
        }
        return clazz.cast(INJECTOR_INSTANCE.getInstance(clazz));
    }
}
