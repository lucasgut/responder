package com.temenos.responder.context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.temenos.responder.commands.Command;
import com.temenos.responder.commands.injector.CommandInjector;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.loader.ClasspathScriptLoader;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.producer.JsonProducer;
import com.temenos.responder.producer.Producer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of Execution Context class.
 *
 * Created by Douglas Groves on 18/12/2016.
 */
public class DefaultExecutionContext implements ExecutionContext {

    private final Map<String, Object> contextAttributes;
    private final Producer producer;
    private final ScriptLoader loader;
    private final String self;
    private final Entity requestBody;
    private final Injector commandInjector;
    private String responseCode;

    public DefaultExecutionContext(String self){
        this.self = self;
        this.contextAttributes = new ConcurrentHashMap<>();
        this.producer = new JsonProducer();
        this.requestBody = null;
        this.loader = new ClasspathScriptLoader("resources");
        this.commandInjector = Guice.createInjector(new CommandInjector());
    }

    public DefaultExecutionContext(String self, Map<String, Object> contextAttributes){
        this.self = self;
        this.contextAttributes = new ConcurrentHashMap<>(contextAttributes);
        this.producer = new JsonProducer();
        this.requestBody = null;
        this.loader = new ClasspathScriptLoader("resources");
        this.commandInjector = Guice.createInjector(new CommandInjector());
    }

    public DefaultExecutionContext(String self, Producer producer, ScriptLoader loader, Parameters parameters, Entity requestBody){
        this.self = self;
        this.contextAttributes = new ConcurrentHashMap<>();
        this.producer = producer;
        this.loader = loader;
        this.requestBody = requestBody;
        for(String paramKey : parameters.getParameterKeys()) {
            setAttribute(paramKey, parameters.getValue(paramKey));
        }
        this.commandInjector = Guice.createInjector(new CommandInjector());
    }

    @Override
    public Producer getProducer() {
        return producer;
    }

    @Override
    public ScriptLoader getScriptLoader() {
        return loader;
    }

    @Override
    public Parameters getParameters() {
        return null;
    }

    @Override
    public synchronized Object getAttribute(String name) {
        return contextAttributes.get(name);
    }

    @Override
    public synchronized boolean setAttribute(String name, Object value) {
        contextAttributes.put(name, value);
        return true;
    }

    @Override
    public String getSelf() {
        return self;
    }

    @Override
    public String getResponseCode() {
        return responseCode;
    }

    @Override
    public Entity getRequestBody() {
        return requestBody;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public Command getCommand(Class<Command> clazz) {
        return clazz.cast(commandInjector.getInstance(clazz));
    }
}
