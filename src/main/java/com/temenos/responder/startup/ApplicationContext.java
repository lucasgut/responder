package com.temenos.responder.startup;

import com.google.inject.*;
import com.temenos.responder.context.Context;
import com.temenos.responder.loader.ClasspathScriptLoader;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.paths.PathHandler;
import com.temenos.responder.paths.ResourcePathHandler;
import com.temenos.responder.producer.*;
import com.temenos.responder.scripting.GroovyScriptingEngine;
import com.temenos.responder.scripting.ScriptingEngine;
import com.temenos.responder.validator.ModelValidator;
import com.temenos.responder.validator.Validator;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Douglas Groves on 15/12/2016.
 */
public class ApplicationContext extends AbstractModule {

    private final ScriptEngine G_SCRIPT_ENGINE = new ScriptEngineManager().getEngineByName("groovy");
    private final ScriptLoader CP_SCRIPT_LOADER = new ClasspathScriptLoader(RESOURCE_ROOT);
    private static final Map<String, Object> attributes = new ConcurrentHashMap<>();

    private static ApplicationContext INSTANCE;
    private static Injector INJECTOR;

    private static final String RESOURCE_ROOT = "resources";

    private ApplicationContext(){}

    @Override
    protected void configure() {
        bind(ScriptLoader.class).toInstance(CP_SCRIPT_LOADER);
        bind(PathHandler.class).to(ResourcePathHandler.class);
        bind(ScriptingEngine.class).to(GroovyScriptingEngine.class);
        bind(Producer.class).to(JsonProducer.class);
        bind(ScriptEngine.class).toInstance(G_SCRIPT_ENGINE);
        bind(Validator.class).to(ModelValidator.class);
        bind(EntityProducer.class).to(EntityJsonProducer.class);
        bind(DocumentProducer.class).to(DocumentJsonProducer.class);
    }

    public static Object getAttribute(String name) {
        return attributes.get(name);
    }

    public static boolean setAttribute(String name, Object value) {
        attributes.put(name, value);
        return true;
    }

    public static <T> T getInjector(Class<T> clazz){
        if(INSTANCE == null){
            INSTANCE = new ApplicationContext();
            INJECTOR = Guice.createInjector(INSTANCE);
        }
        return clazz.cast(INJECTOR.getInstance(clazz));
    }
}
