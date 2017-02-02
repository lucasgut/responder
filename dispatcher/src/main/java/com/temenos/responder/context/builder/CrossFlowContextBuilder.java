package com.temenos.responder.context.builder;

import com.temenos.responder.configuration.Origin;
import com.temenos.responder.context.CrossFlowContext;
import com.temenos.responder.context.DefaultCrossFlowContext;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.context.manager.ContextManager;
import com.temenos.responder.context.manager.DefaultContextManager;
import com.temenos.responder.flows.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * A CrossFlowContextBuilder is responsible for building and managing {@link CrossFlowContext cross-flow context} instances.
 * For a {@link CrossFlowContext cross-flow context} to be valid, the origin, from and into parameters must be set.
 *
 * @author Douglas Groves
 */
public class CrossFlowContextBuilder implements ContextBuilder<CrossFlowContext>, AutoCloseable {

    private Origin origin;
    private Parameters parameters;
    private List<String> into = new ArrayList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(CrossFlowContextBuilder.class);

    private CrossFlowContextBuilder(){}

    public CrossFlowContextBuilder origin(String serverRoot, String self, Class<? extends Flow> sourceFlow){
        this.origin = new Origin(sourceFlow, Thread.currentThread());
        return this;
    }

    public CrossFlowContextBuilder parameters(String key, String value){
        parameters.setValue(key, value);
        return this;
    }

    public CrossFlowContextBuilder parameters(Map<String, String> values){
        for(Map.Entry<String,String> entry : values.entrySet()){
            parameters.setValue(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public CrossFlowContextBuilder parameters(Parameters parameters){
        this.parameters = parameters;
        return this;
    }

    public CrossFlowContextBuilder into(String... into){
        this.into.addAll(Arrays.asList(into));
        return this;
    }

    public CrossFlowContextBuilder manager(ContextManager manager){
        return new ManagedCrossFlowContextBuilder(manager);
    }

    @Override
    public CrossFlowContext build() {
        return new DefaultCrossFlowContext(origin, parameters, into);
    }

    public long buildAndGetId(){
        throw new RuntimeException("No context manager specified");
    }

    @Override
    public void close() throws RuntimeException {
        LOGGER.warn("Tried to cleanup an unmanaged execution context");
    }

    public static CrossFlowContextBuilder builder(){
        return new CrossFlowContextBuilder();
    }

    private static class ManagedCrossFlowContextBuilder extends CrossFlowContextBuilder {

        private ContextManager manager;
        private long managedContextId;

        private ManagedCrossFlowContextBuilder(ContextManager manager){
            this.manager = manager;
        }

        @Override
        public long buildAndGetId() {
            CrossFlowContext ctx = super.build();
            this.managedContextId = DefaultContextManager.getManager().manageContext(ctx);
            return managedContextId;
        }

        @Override
        public void close() throws RuntimeException {
            manager.deleteManagedContext(managedContextId);
        }
    }
}
