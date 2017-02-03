package com.temenos.responder.context.builder;

import com.temenos.responder.context.Parameters;
import com.temenos.responder.flows.Flow;

/**
 * Created by dgroves on 02/02/2017.
 */
public interface ExecutionParameterBuilder {
    ExecutionParameterBuilder flow(Class<? extends Flow> flow);
    ExecutionParameterBuilder parameters(Parameters parameters);
    ExecutionParameterBuilder parameter(String name, String value);
    ExecutionParameterBuilder into(String attributeName);
    ExecutionBuilder execution();
}
