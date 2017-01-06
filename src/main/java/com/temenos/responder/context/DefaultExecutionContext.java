package com.temenos.responder.context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.temenos.responder.commands.Command;
import com.temenos.responder.commands.injector.CommandInjector;
import com.temenos.responder.entity.runtime.Entity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of Execution Context class.
 *
 * Created by Douglas Groves on 18/12/2016.
 */
public class DefaultExecutionContext implements ExecutionContext {

    private final Map<String, Object> contextAttributes;
    private final String resourceName;
    private final String self;
    private final Entity requestBody;
    private final Injector commandInjector;
    private String responseCode;

    public DefaultExecutionContext(String self, String resourceName){
        this.self = self;
        this.resourceName = resourceName;
        this.contextAttributes = new ConcurrentHashMap<>();
        this.requestBody = null;
        this.commandInjector = Guice.createInjector(new CommandInjector());
    }

    public DefaultExecutionContext(String self, String resourceName, Map<String, Object> contextAttributes){
        this.self = self;
        this.resourceName = resourceName;
        this.contextAttributes = new ConcurrentHashMap<>(contextAttributes);
        this.requestBody = null;
        this.commandInjector = Guice.createInjector(new CommandInjector());
    }

    public DefaultExecutionContext(String self, String resourceName, Parameters parameters, Entity requestBody){
        this.self = self;
        this.resourceName = resourceName;
        this.contextAttributes = new ConcurrentHashMap<>();
        this.requestBody = requestBody;
        for(String paramKey : parameters.getParameterKeys()) {
            setAttribute(paramKey, parameters.getValue(paramKey));
        }
        this.commandInjector = Guice.createInjector(new CommandInjector());
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
    public String getResourceName() {
        return resourceName;
    }

    @Override
    public String getResponseCode() {
        return responseCode;
    }

    @Override
    public Entity getRequestBody() {
        return requestBody;
    }

    @Override
    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public Command getCommand(Class<Command> clazz) {
        return clazz.cast(commandInjector.getInstance(clazz));
    }

    @Override
    public Object getFieldFromRequestBody(String fieldName) {
        return requestBody.get(fieldName);
    }
}
