package com.temenos.responder.executor;

import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.flows.Flow;

/**
 * Created by dgroves on 17/01/2017.
 */
public class FlowExecutor implements Executor {

    @Override
    public void execute(Flow flow, ExecutionContext context) {
        flow.execute(context);
    }
}
