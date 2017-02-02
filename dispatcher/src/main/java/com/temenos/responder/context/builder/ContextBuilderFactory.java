package com.temenos.responder.context.builder;

import com.temenos.responder.context.Context;
import com.temenos.responder.context.CrossFlowContext;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.manager.ContextManager;

/**
 * Created by dgroves on 01/02/2017.
 */
public class ContextBuilderFactory {

    private static ContextBuilderFactory INSTANCE;

    private ContextBuilderFactory(){}

    public ExecutionContextBuilder getExecutionContextBuilder(ContextManager manager){
        return ExecutionContextBuilder.builder().manager(manager);
    }

    public CrossFlowContextBuilder getCrossFlowContextBuilder(ContextManager manager){
        return CrossFlowContextBuilder.builder().manager(manager);
    }

    public static ContextBuilderFactory factory(){
        if(INSTANCE == null){
            INSTANCE = new ContextBuilderFactory();
        }
        return INSTANCE;
    }
}
