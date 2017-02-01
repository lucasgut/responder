package com.temenos.responder.dispatcher;

import com.temenos.responder.context.*;
import com.temenos.responder.entity.runtime.Document;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.executor.FlowExecutor;
import com.temenos.responder.flows.Flow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Douglas Groves
 */
public class FlowDispatcher implements Dispatcher {

    private final ExecutorService runner;
    private final List<FlowExecutor> executors;
    private final RequestContext context;
    private final AtomicInteger next;
    private final ExecutionContextBuilder builder;

    private static final int FIRST_EXECUTOR = 0;
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowDispatcher.class);

    public FlowDispatcher(int executors, RequestContext context) {
        this.executors = new ArrayList<>();
        for (int i = 0; i < executors; i++) {
            this.executors.add(new FlowExecutor());
        }
        this.next = new AtomicInteger(FIRST_EXECUTOR);
        this.runner = Executors.newCachedThreadPool();
        this.context = context;
        this.builder = new ExecutionContextBuilder();
    }

    public FlowDispatcher(List<FlowExecutor> executors, RequestContext context, ExecutionContextBuilder builder) {
        this.executors = executors;
        this.next = new AtomicInteger(FIRST_EXECUTOR);
        this.runner = Executors.newFixedThreadPool(executors.size());
        this.context = context;
        this.builder = builder;
    }

    @Override
    public Document notify(Class<Flow> flow) {
        Document result = null;
        try {
            ExecutionContext ctx = getExecutionContext();
            result = runner.submit(getCallable(flow)).get();
        } catch (InterruptedException | ExecutionException e) {
            LOGGER.error("Unable to execute Flow: {}", flow, e);
        }
        return result;
    }

    @Override
    public Map<String, List<Document>> notify(List<Class<Flow>> flows) {
        Map<String,List<Document>> result = new ConcurrentHashMap<>();
        try {
            runner.invokeAll(getCallables(flows)).forEach(flowResult -> {
                try {
                    Document flowResponse = flowResult.get();
                    List<Document> documents = result.get(flowResponse.getFlowName());
                    if(documents == null){
                        documents = new ArrayList<>();
                        result.put(flowResponse.getFlowName(), documents);
                    }
                    documents.add(flowResponse);
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

    private Callable<Document> getCallable(Class<Flow> flow){
        return () -> {
            ExecutionContext ctx = getExecutionContext();
            try {
                getNextFlowExecutor().execute(flow.newInstance(), ctx);
            } catch (IllegalAccessException | InstantiationException ie) {
                LOGGER.error("Unable to execute Flow: {}", flow, ie);
            }
            Object embeddedContents = ctx.getAttribute("document.embedded");
            if(embeddedContents == null){
                embeddedContents = new Entity();
            }
            return new Document((Entity) ctx.getAttribute("document.links.self"),
                    (Entity)embeddedContents,
                    (Entity) ctx.getAttribute("finalResult"),
                    context.getResourceName(),
                    flow.getSimpleName());
        };
    }

    private Collection<Callable<Document>> getCallables(List<Class<Flow>> flows) {
        List<Callable<Document>> result = new ArrayList<>();
        flows.forEach(flow -> result.add(getCallable(flow)));
        return result;
    }

    /**
     * Fetch the next executor in the list using a round-robin algorithm.
     *
     * @return The next executor that a {@link Flow flow} will be executed with.
     */
    private FlowExecutor getNextFlowExecutor() {
        return executors.get(getNextIndex());
    }

    private ExecutionContext getExecutionContext() {
        return builder.origin(context.getOrigin())
                .resourceName(context.getResourceName())
                .requestParameters(context.getRequestParameters())
                .requestBody(context.getRequestBody())
                .dispatcher(this)
                .build();
    }

    private int getNextIndex() {
        if (next.get() + 1 >= executors.size()) {
            next.set(0);
        } else {
            next.incrementAndGet();
        }
        return next.get();
    }

}
