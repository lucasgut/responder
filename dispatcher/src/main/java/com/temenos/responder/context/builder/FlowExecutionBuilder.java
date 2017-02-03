package com.temenos.responder.context.builder;

import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.flows.Flow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dgroves on 02/02/2017.
 */
public class FlowExecutionBuilder implements ExecutionBuilder {

    private FlowExecutionParameterBuilder parameterBuilder;
    private ExecutionContext ctx;

    public FlowExecutionBuilder(ExecutionContext ctx){
        this.ctx = ctx;
    }

    public FlowExecutionBuilder(ExecutionContext ctx, FlowExecutionParameterBuilder parameterBuilder){
        this.ctx = ctx;
        this.parameterBuilder = parameterBuilder;
    }

    @Override
    public ExecutionParameterBuilder inParallelWith(Class<? extends Flow> flow) {
        return new FlowExecutionParameterBuilder(this, ctx).flow(flow);
    }

    @Override
    public void execute() {
        List<FlowExecutionParameterBuilder.FlowExecutionParameters> executionParameters = new ArrayList<>();
        FlowExecutionBuilder executionBuilder = this;
        do {
            FlowExecutionParameterBuilder parameterBuilder = executionBuilder.parameterBuilder;
            executionParameters.add(parameterBuilder.build());
            executionBuilder = parameterBuilder.getExecutionBuilder();
        }while(executionBuilder != null);
        if(executionParameters.size() == 1){
            FlowExecutionParameterBuilder.FlowExecutionParameters params = executionParameters.get(0);
            ctx.executeFlow(params.getFlow(), params.getParameters(), params.getIntoAttribute());
        }else{
            List<Class<? extends Flow>> flows = new ArrayList<>();
            List<Parameters> parameters = new ArrayList<>();
            List<String> into = new ArrayList<>();
            executionParameters.forEach(it -> {
                flows.add(it.getFlow());
                parameters.add(it.getParameters());
                into.add(it.getIntoAttribute());
            });
            ctx.executeFlows(flows, parameters, into);
        }
    }

}
