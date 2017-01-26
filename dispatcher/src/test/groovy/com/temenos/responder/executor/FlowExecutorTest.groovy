package com.temenos.responder.executor

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.flows.Flow
import spock.lang.Specification

/**
 * Created by dgroves on 18/01/2017.
 */
class FlowExecutorTest extends Specification {
    def "Execute a flow and discard the result"(){
        given:
            def executor = new FlowExecutor()
            def flow = Mock(Flow)
            def executionContext = Mock(ExecutionContext)
        when:
            executor.execute(flow, executionContext)
        then:
            1 * flow.execute(executionContext)
    }
}
