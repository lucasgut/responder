package com.temenos.responder.context.builder;

import com.temenos.responder.context.*;
import com.temenos.responder.context.manager.ContextManager;
import com.temenos.responder.context.manager.DefaultContextManager;
import com.temenos.responder.dispatcher.Dispatcher;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * An ExecutionContextBuilder creates and manages execution context instances.
 *
 * @author Douglas Groves
 */
public class ExecutionContextBuilder implements ContextBuilder<ExecutionContext>, AutoCloseable {

    private String serverRoot;
    private String origin;
    private String resourceName;
    private Parameters requestParameters;
    private Entity requestBody;
    private Dispatcher dispatcher;
    private ContextManager manager;
    private Class<? extends Flow> flow;

    private static final short INITIAL_ID = 0;
    private static final long MAXIMUM_POOL_SIZE = 5;
    private static Logger LOGGER = LoggerFactory.getLogger(ExecutionContextBuilder.class);

    private ExecutionContextBuilder() {}

    @Override
    public ExecutionContext build() {
        return new DefaultExecutionContext(serverRoot, origin, resourceName, requestParameters, requestBody, dispatcher, flow);
    }

    @Override
    public long buildAndGetId() {
        throw new RuntimeException("No context manager specified.");
    }

    public ExecutionContextBuilder serverRoot(String serverRoot) {
        this.serverRoot = serverRoot;
        return this;
    }

    public ExecutionContextBuilder origin(String origin) {
        this.origin = origin;
        return this;
    }

    public ExecutionContextBuilder resourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public ExecutionContextBuilder requestParameters(Parameters requestParameters) {
        this.requestParameters = requestParameters;
        return this;
    }

    public ExecutionContextBuilder requestBody(Entity requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    public ExecutionContextBuilder dispatcher(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        return this;
    }

    public ExecutionContextBuilder flow(Class<? extends Flow> flowClass) {
        this.flow = flow;
        return this;
    }

    public ExecutionContextBuilder manager(ContextManager manager){
        this.manager = manager;
        return new ManagedExecutionContextBuilder(manager);
    }

    @Override
    public void close() throws RuntimeException {
        LOGGER.warn("Tried to close an unmanaged execution context.");
    }

    private static class ManagedExecutionContextBuilder extends ExecutionContextBuilder implements AutoCloseable {

        private ContextManager manager;
        private long managedContextId;

        private ManagedExecutionContextBuilder(ContextManager manager){
            this.manager = manager;
        }

        @Override
        public long buildAndGetId() {
            ExecutionContext ctx = super.build();
            managedContextId = manager.manageContext(ctx);
            return managedContextId;
        }

        @Override
        public void close() throws RuntimeException {
            manager.deleteManagedContext(managedContextId);
        }
    }

    public static ExecutionContextBuilder builder(){
        return new ExecutionContextBuilder();
    }

}
