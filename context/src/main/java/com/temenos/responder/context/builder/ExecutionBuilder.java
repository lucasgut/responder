package com.temenos.responder.context.builder;

import com.temenos.responder.flows.Flow;

/**
 * Created by dgroves on 02/02/2017.
 */
public interface ExecutionBuilder {
    ExecutionParameterBuilder inParallelWith(Class<? extends Flow> flow);
    void execute();
}
