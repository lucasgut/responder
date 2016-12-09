package com.temenos.responder.startup;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.temenos.responder.annotations.GroovyScript;
import com.temenos.responder.loader.ClasspathScriptLoader;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.scripting.GroovyScriptingEngine;
import com.temenos.responder.scripting.ScriptingEngine;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
public class ResponderBindings extends AbstractModule {

    private static final ScriptEngine engine = new ScriptEngineManager().getEngineByName("Groovy");

    @Override
    protected void configure() {
        bind(ScriptEngine.class).annotatedWith(GroovyScript.class).toInstance(engine);
    }
}
