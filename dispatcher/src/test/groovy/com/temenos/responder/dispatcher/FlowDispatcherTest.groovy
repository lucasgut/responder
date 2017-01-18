package com.temenos.responder.dispatcher

import com.temenos.responder.configuration.Resource
import com.temenos.responder.context.DefaultRequestContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.context.Parameters
import com.temenos.responder.context.RequestContext
import com.temenos.responder.entity.runtime.Document
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.executor.FlowExecutor
import com.temenos.responder.flows.AdditionFlow
import com.temenos.responder.flows.CustomerInformation
import com.temenos.responder.flows.Flow
import com.temenos.responder.flows.VersionInformationFlow
import com.temenos.responder.context.ExecutionContextBuilder
import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

/**
 * Created by dgroves on 17/01/2017.
 */
class FlowDispatcherTest extends Specification {

    @Unroll
    def "Invoking notify with #flowClass.simpleName creates a Flow instance and executes it using an executor"(Class flowClass) {
        given:
            def mockExecutor = Mock(FlowExecutor)
            def requestContext = Mock(RequestContext)
            def executionContext = Mock(ExecutionContext)
            def contextBuilder = Mock(ExecutionContextBuilder)
            def dispatcher = new FlowDispatcher([mockExecutor], requestContext, contextBuilder)
            def parameters = Mock(Parameters)
            def links = Mock(Entity), body = Mock(Entity)
        when:
            def result = dispatcher.notify(flowClass)
        then:
            contextBuilder.origin(_) >> contextBuilder
            contextBuilder.dispatcher(_) >> contextBuilder
            contextBuilder.requestBody(_) >> contextBuilder
            contextBuilder.requestParameters(_) >> contextBuilder
            contextBuilder.resourceName(_) >> contextBuilder
            contextBuilder.build() >> executionContext

            1 * executionContext.getAttribute("document.links.self") >> links
            1 * executionContext.getAttribute("finalResult") >> body
            1 * links.getValues() >> [:]
            1 * body.getValues() >> ["Greeting":"Hello","Subject":"World"]
            1 * mockExecutor.execute({ flowClass.isAssignableFrom(it.class) }, _)

            requestContext.getRequestParameters() >> parameters
            requestContext.getResourceName() >> "helloWorld"
            result instanceof Document
        where:
            flowClass << [AdditionFlow]
    }

    @Unroll
    def "Invoking notify with #flowClasses.simpleName instantiates objects for each class and calls the executor"(List flowClasses) {
        given:
            def mockExecutor = []
            (flowClasses.size()).times { mockExecutor.add(Mock(FlowExecutor)) }
            def requestContext = Mock(RequestContext)
            def executionContext = Mock(ExecutionContext)
            def contextBuilder = Mock(ExecutionContextBuilder)
            def dispatcher = new FlowDispatcher(mockExecutor, requestContext, contextBuilder)
            def parameters = Mock(Parameters)
            def links = Mock(Entity), body = Mock(Entity)
        when:
            def result = dispatcher.notify(flowClasses)
        then:
            contextBuilder.origin(_) >> contextBuilder
            contextBuilder.dispatcher(_) >> contextBuilder
            contextBuilder.requestBody(_) >> contextBuilder
            contextBuilder.requestParameters(_) >> contextBuilder
            contextBuilder.resourceName(_) >> contextBuilder
            contextBuilder.build() >> executionContext

            (flowClasses.size()) * executionContext.getAttribute("document.links.self") >> links
            (flowClasses.size()) * executionContext.getAttribute("finalResult") >> body
            mockExecutor.each { FlowExecutor mockFlowExecutor -> 1 * mockFlowExecutor.execute(_ as Flow,_) }

            links.getValues() >> [:]
            body.getValues() >> ["Greeting":"Hello","Subject":"World"]
            requestContext.getRequestParameters() >> parameters
            requestContext.getResourceName() >> "helloWorld"
            result.size() == flowClasses.size()
        where:
            flowClasses << [
                [AdditionFlow, AdditionFlow, AdditionFlow],
                [VersionInformationFlow, AdditionFlow],
                [CustomerInformation]
            ]
    }
}
