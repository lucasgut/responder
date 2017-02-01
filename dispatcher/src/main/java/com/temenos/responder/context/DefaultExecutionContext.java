package com.temenos.responder.context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.temenos.responder.commands.Command;
import com.temenos.responder.commands.injector.CommandInjector;
import com.temenos.responder.dispatcher.Dispatcher;
import com.temenos.responder.dispatcher.FlowDispatcher;
import com.temenos.responder.entity.runtime.Document;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

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
    private final Lock lock;
    private final Dispatcher dispatcher;
    private final String serverRoot;
    private int responseCode;

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExecutionContext.class);
    private static final int LOCK_TIMEOUT = 10;

    public DefaultExecutionContext(String serverRoot, String self, String resourceName, Dispatcher dispatcher){
        this.self = self;
        this.resourceName = resourceName;
        this.contextAttributes = new ConcurrentHashMap<>();
        this.requestBody = null;
        this.commandInjector = Guice.createInjector(new CommandInjector());
        this.lock = new ReentrantLock();
        this.dispatcher = dispatcher;
        this.serverRoot = serverRoot;
    }

    public DefaultExecutionContext(String serverRoot, String self, String resourceName, Map<String, Object> contextAttributes, Dispatcher dispatcher){
        this.self = self;
        this.resourceName = resourceName;
        this.contextAttributes = new ConcurrentHashMap<>(contextAttributes);
        this.requestBody = null;
        this.commandInjector = Guice.createInjector(new CommandInjector());
        this.lock = new ReentrantLock();
        this.dispatcher = dispatcher;
        this.serverRoot = serverRoot;
    }

    public DefaultExecutionContext(String serverRoot, String self, String resourceName, Parameters parameters, Entity requestBody, Dispatcher dispatcher){
        this.self = self;
        this.resourceName = resourceName;
        this.contextAttributes = new ConcurrentHashMap<>();
        this.requestBody = requestBody;
        for(String paramKey : parameters.getParameterKeys()) {
            setAttribute(paramKey, parameters.getValue(paramKey));
        }
        this.commandInjector = Guice.createInjector(new CommandInjector());
        this.lock = new ReentrantLock();
        this.dispatcher = dispatcher;
        this.serverRoot = serverRoot;
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
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public Entity getRequestBody() {
        return requestBody;
    }

    @Override
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public <T extends Command> Command getCommand(Class<T> clazz) {
        return clazz.cast(commandInjector.getInstance(clazz));
    }

    @Override
    public Object getFieldFromRequestBody(String fieldName) {
        return requestBody.get(fieldName);
    }

    @Override
    public Document notifyDispatchers(Class<Flow> flow) {
        return this.dispatcher.notify(flow);
    }

    @Override
    public void notifyDispatchers(Class<Flow> flow, String name) {
        this.contextAttributes.put(name, this.dispatcher.notify(flow));
    }

    @Override
    public Map<String, List<Document>> notifyDispatchers(List<Class<Flow>> flows) {
        return this.dispatcher.notify(flows);
    }

    @Override
    public void notifyDispatchers(List<Class<Flow>> flows, String name) {
        contextAttributes.put(name, this.dispatcher.notify(flows));
    }

    @Override
    public String getInternalResource(String resourcePath) {
        return this.serverRoot + resourcePath;
    }
}
