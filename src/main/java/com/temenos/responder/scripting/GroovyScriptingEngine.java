package com.temenos.responder.scripting;

import com.google.inject.Inject;
import com.temenos.responder.annotations.GroovyScript;
import com.temenos.responder.exception.ScriptExecutionException;
import com.temenos.responder.loader.ClasspathScriptLoader;
import com.temenos.responder.loader.ScriptLoader;

import java.io.IOException;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class GroovyScriptingEngine implements ScriptingEngine {

    private final ScriptEngine engine;
    private final ScriptLoader loader;

    public GroovyScriptingEngine() {
        this.engine = new ScriptEngineManager().getEngineByName("Groovy");
        this.loader = new ClasspathScriptLoader();
    }

    @Inject
    public GroovyScriptingEngine(@GroovyScript ScriptEngine engine) {
        this.engine = engine;
        this.loader = new ClasspathScriptLoader();
    }

    public GroovyScriptingEngine(@GroovyScript ScriptEngine engine, ScriptLoader loader) {
        this.engine = engine;
        this.loader = loader;
    }

    @Override
    public Object execute(String scriptName, String methodName, Object... params) throws ScriptExecutionException {
        String qualifiedScriptName = scriptName + ".groovy";
        Invocable invocable = (Invocable)engine;
        Object result = null;
        try {
            engine.eval(this.loader.load(qualifiedScriptName));
            result = invocable.invokeFunction(methodName, params);
        }catch(IOException ioe){
            throw new ScriptExecutionException("Could not execute file "+qualifiedScriptName+" as a matching script file could not be found.", ioe);
        }catch(ScriptException | NoSuchMethodException se){
            throw new ScriptExecutionException("Execution of file "+scriptName+" failed.", se);
        }
        return result;
    }

}
