package com.temenos.responder.dispatcher;

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
    /**
     * Notify the dispatcher instance that a flow is available.
     *
     * @param flow A flow that has become available.
     */
    Document notify(Class<Flow> flow);

    Map<String, List<Document>> notify(List<Class<Flow>> flow);
}
