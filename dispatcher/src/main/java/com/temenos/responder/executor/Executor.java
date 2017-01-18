package com.temenos.responder.executor;

import com.temenos.responder.context.CommandContext;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.flows.Flow;

import java.util.List;

/**
 * Created by dgroves on 17/01/2017.
 */
public interface Executor {
    /**
     * Execute a flow.
     *
     * @param context A {@link Flow flow object} containing one or more tasks
     *                contributing towards a resource transaction.
     */
    void execute(Flow flow, ExecutionContext context);
}
