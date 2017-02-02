package com.temenos.responder.context.builder;

import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.flows.Flow;

/**
 * Created by dgroves on 02/02/2017.
 */
public class FlowExecutionParameterBuilder implements ExecutionParameterBuilder {

    private ExecutionContext ctx;
    private Class<? extends Flow> flow;
    private Parameters parameters = new Parameters();
    private String intoAttribute;
    private FlowExecutionBuilder executionBuilder;

    public FlowExecutionParameterBuilder(ExecutionContext ctx){
        this.parameters = new Parameters();
        this.ctx = ctx;
    }

    public FlowExecutionParameterBuilder(FlowExecutionBuilder executionBuilder, ExecutionContext ctx){
        this.executionBuilder = executionBuilder;
        this.ctx = ctx;
    }

    FlowExecutionParameters build(){
        return new FlowExecutionParameters(flow, parameters, intoAttribute);
    }

    @Override
    public ExecutionParameterBuilder flow(Class<? extends Flow> flow) {
        this.flow = flow;
        return this;
    }

    @Override
    public ExecutionParameterBuilder parameters(Parameters parameters) {
        this.parameters = parameters;
        return this;
    }

    @Override
    public ExecutionParameterBuilder parameter(String name, String value) {
        this.parameters.setValue(name, value);
        return this;
    }

    @Override
    public ExecutionParameterBuilder into(String attributeName) {
        this.intoAttribute = attributeName;
        return this;
    }

    @Override
    public ExecutionBuilder execution() {
        return new FlowExecutionBuilder(ctx, this);
    }

    public FlowExecutionBuilder getExecutionBuilder(){
        return this.executionBuilder;
    }

    public static class FlowExecutionParameters {

        private Class<? extends Flow> flow;
        private Parameters parameters;
        private String intoAttribute;

        private FlowExecutionParameters(Class<? extends Flow> flow, Parameters parameters, String intoAttribute){
            this.flow = flow;
            this.parameters = parameters;
            this.intoAttribute = intoAttribute;
        }

        public Class<? extends Flow> getFlow(){
            return flow;
        }

        public Parameters getParameters(){
            return parameters;
        }

        public String getIntoAttribute(){
            return intoAttribute;
        }
    }

}
