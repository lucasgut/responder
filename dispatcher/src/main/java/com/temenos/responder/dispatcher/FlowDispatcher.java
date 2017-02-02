package com.temenos.responder.dispatcher;

import com.temenos.responder.context.*;
import com.temenos.responder.context.builder.ContextBuilderFactory;
import com.temenos.responder.context.builder.ExecutionContextBuilder;
import com.temenos.responder.context.manager.ContextManager;
import com.temenos.responder.context.manager.DefaultContextManager;
import com.temenos.responder.entity.runtime.Document;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.executor.FlowExecutor;
import com.temenos.responder.flows.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Douglas Groves
 */
public class FlowDispatcher implements Dispatcher {

    private final ExecutorService runner;
    private final FlowExecutor executor;
    private final RequestContext context;
    private final ContextBuilderFactory factory;
    private final ContextManager manager;

    private static final int NOT_A_FLOW = -1;
    private static final int THREADS = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowDispatcher.class);

    public FlowDispatcher(int threads, RequestContext context) {
        this.executor = new FlowExecutor();
        this.runner = Executors.newFixedThreadPool(threads);
        this.context = context;
        this.factory = ContextBuilderFactory.factory();
        this.manager = DefaultContextManager.getManager();
    }

    public FlowDispatcher(FlowExecutor executor, RequestContext context, ContextManager manager, ContextBuilderFactory factory) {
        this.executor = executor;
        this.runner = Executors.newFixedThreadPool(THREADS);
        this.context = context;
        this.factory = factory;
        this.manager = manager;
    }

    @Override
    public Document notify(Class<? extends Flow> flow) {
        return notify(flow, NOT_A_FLOW);
    }

    @Override
    public Document notify(Class<? extends Flow> flow, long crossFlowContextId) {
        Document result = null;
        ParameterisedContext context;
        if(crossFlowContextId != NOT_A_FLOW) {
            context = manager.getManagedContext(crossFlowContextId, CrossFlowContext.class);
        }else{
            context = this.context;
        }
        try {
            result = runner.submit(
                getCallable(flow, context.parameters())
            ).get();
        } catch (Throwable e) {
            LOGGER.error("Unable to execute Flow: {}", flow, e);
        }
        return result;
    }

    @Override
    public Map<String, Document> notify(List<Class<? extends Flow>> flows, long crossFlowContextId, String... into) {
        Map<String, Document> result = new ConcurrentHashMap<>();
        try {
            int[] index = {0};
            runner.invokeAll(
                    getCallables(flows, manager.getManagedContext(crossFlowContextId, CrossFlowContext.class))
            ).forEach(flowResult -> {
                try {
                    Document flowResponse = flowResult.get();
                    result.put(into[index[0]], flowResponse);
                    index[0]++;
                } catch (InterruptedException | ExecutionException ie) {
                    throw new RuntimeException(ie);
                }
            });
        } catch (InterruptedException e) {
            LOGGER.error("Failed to execute all attached flows", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    private Callable<Document> getCallable(Class<? extends Flow> flow, Parameters parameters) {
        return () -> {
            try(ExecutionContextBuilder builder = this.factory.getExecutionContextBuilder(manager)) {
                ExecutionContext ctx = manager.getManagedContext(getExecutionContext(flow, builder, parameters), ExecutionContext.class);
                try {
                    executor.execute(flow.newInstance(), ctx);
                } catch (IllegalAccessException | InstantiationException ie) {
                    LOGGER.error("Unable to execute Flow: {}", flow, ie);
                }
                Object embeddedContents = ctx.getAttribute("document.embedded");
                if (embeddedContents == null) {
                    embeddedContents = new Entity();
                }
                return new Document((Entity) ctx.getAttribute("document.links.self"),
                        (Entity) embeddedContents,
                        (Entity) ctx.getAttribute("finalResult"),
                        context.getResourceName(),
                        flow.getSimpleName());
            }
        };
    }

    private Collection<Callable<Document>> getCallables(List<Class<? extends Flow>> flows, CrossFlowContext cfc) {
        List<Callable<Document>> result = new ArrayList<>();
        flows.forEach(flow -> result.add(getCallable(flow, cfc.parameters())));
        return result;
    }

    private long getExecutionContext(Class<? extends Flow> flow, ExecutionContextBuilder builder, Parameters parameters) {
        return builder.flow(flow)
                .serverRoot(context.getServerRoot())
                .origin(context.getOrigin())
                .resourceName(context.getResourceName())
                .requestParameters(parameters)
                .requestBody(context.getRequestBody())
                .dispatcher(this)
                .buildAndGetId();
    }
}
