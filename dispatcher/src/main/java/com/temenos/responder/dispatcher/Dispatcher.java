package com.temenos.responder.dispatcher;

import com.temenos.responder.context.CrossFlowContext;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.entity.runtime.Document;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.Flow;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * Describes a class responsible for executing a task.
 *
 * @author Douglas Groves
 */
public interface Dispatcher {

    Document notify(Class<? extends Flow> flow);

    /**
     * Notify the dispatcher instance that a {@link Flow flow} is available to execute.
     *
     * @param flow A flow that has become available.
     *
     */
    Document notify(Class<? extends Flow> flow, long crossFlowContextId);

    /**
     * Notify the dispatcher instance that multiple {@link Flow flows} are available to execute in parallel.
     *
     * @param flows
     * @param crossFlowContextId
     * @return
     */
    Map<String, Document> notify(List<Class<? extends Flow>> flows, long crossFlowContextId);
}
