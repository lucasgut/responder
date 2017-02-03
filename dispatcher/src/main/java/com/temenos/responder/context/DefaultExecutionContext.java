package com.temenos.responder.context;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.temenos.responder.commands.Command;
import com.temenos.responder.commands.injector.CommandInjector;
import com.temenos.responder.context.builder.ContextBuilderFactory;
import com.temenos.responder.context.builder.CrossFlowContextBuilder;
import com.temenos.responder.context.builder.ExecutionParameterBuilder;
import com.temenos.responder.context.builder.FlowExecutionParameterBuilder;
import com.temenos.responder.context.manager.ContextManager;
import com.temenos.responder.context.manager.DefaultContextManager;
import com.temenos.responder.dispatcher.Dispatcher;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default {@link ExecutionContext execution context} implementation.
 * <p>
 *
 * @author Douglas Groves
 */
public class DefaultExecutionContext implements ExecutionContext {

    private final Map<String, Object> contextAttributes;
    private final String resourceName;
    private final String self;
    private final Entity requestBody;
    private final Injector commandInjector;
    private final Dispatcher dispatcher;
    private final String serverRoot;
    private final ContextBuilderFactory factory;
    private final ContextManager manager;
    private int responseCode;
    private final Class<? extends Flow> flowClass;

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExecutionContext.class);

    public DefaultExecutionContext(String serverRoot, String self, String resourceName, Dispatcher dispatcher, Class<? extends Flow> flowClass) {
        this(serverRoot, self, resourceName, new Parameters(), new HashMap<>(), null, dispatcher, ContextBuilderFactory.factory(), DefaultContextManager.getManager(), flowClass);
    }

    public DefaultExecutionContext(String serverRoot, String self,
                                   String resourceName, Parameters parameters, Entity requestBody, Dispatcher dispatcher, Class<? extends Flow> flowClass) {
        this(serverRoot, self, resourceName, parameters, new HashMap<>(),
                requestBody, dispatcher, ContextBuilderFactory.factory(), DefaultContextManager.getManager(), flowClass);
    }

    public DefaultExecutionContext(String serverRoot, String self, String resourceName,
                                   Parameters parameters, Map<String, Object> contextAttributes, Entity requestBody,
                                   Dispatcher dispatcher, ContextBuilderFactory factory, ContextManager manager, Class<? extends Flow> flowClass) {
        this.serverRoot = serverRoot;
        this.self = self;
        this.resourceName = resourceName;
        this.contextAttributes = contextAttributes;
        if (parameters != null) {
            for (String paramKey : parameters.getParameterKeys()) {
                setAttribute(paramKey, parameters.getValue(paramKey));
            }
        }
        this.requestBody = requestBody;
        this.commandInjector = Guice.createInjector(new CommandInjector());
        this.dispatcher = dispatcher;
        this.factory = factory;
        this.manager = manager;
        this.flowClass = flowClass;
    }

    @Override
    public Object getAttribute(String name) {
        return contextAttributes.get(name);
    }

    @Override
    public boolean setAttribute(String name, Object value) {
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
    public Class<? extends Flow> getFlowClass(){
        return flowClass;
    }

    @Override
    public void executeFlow(Class<? extends Flow> targetFlow, Parameters from, String into) {
        try (CrossFlowContextBuilder builder = factory.getCrossFlowContextBuilder(manager)) {
            this.contextAttributes.put(into, this.dispatcher.notify(targetFlow, crossFlowContext(builder, flowClass, into, from)));
        }
    }

    @Override
    public void executeFlows(List<Class<? extends Flow>> targetFlows, List<Parameters> from, List<String> into) {
        try (CrossFlowContextBuilder builder = factory.getCrossFlowContextBuilder(manager)) {
            this.contextAttributes.putAll(this.dispatcher.notify(targetFlows, crossFlowContext(builder, flowClass, into, from)));
        }
    }

    @Override
    public String getInternalResource(String resourcePath) {
        return this.serverRoot + resourcePath;
    }

    @Override
    public ExecutionParameterBuilder buildExecution() {
        return new FlowExecutionParameterBuilder(this);
    }

    private long crossFlowContext(CrossFlowContextBuilder builder, Class<? extends Flow> sourceFlow, String into, Parameters from) {
        return builder
                .origin(this.serverRoot, this.self, sourceFlow)
                .parameters(from)
                .into(into)
                .buildAndGetId();
    }

    private long crossFlowContext(CrossFlowContextBuilder builder, Class<? extends Flow> sourceFlow, List<String> into, List<Parameters> from) {
        return builder
                .origin(this.serverRoot, this.self, sourceFlow)
                .parameters(from)
                .into(into)
                .buildAndGetId();
    }
}
