package com.temenos.responder.dispatcher

import com.temenos.responder.context.CrossFlowContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.context.Parameters
import com.temenos.responder.context.RequestContext
import com.temenos.responder.context.builder.ContextBuilderFactory
import com.temenos.responder.context.manager.ContextManager
import com.temenos.responder.entity.runtime.Document
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.executor.FlowExecutor
import com.temenos.responder.flows.AdditionFlow
import com.temenos.responder.flows.CustomerInformation
import com.temenos.responder.flows.Flow
import com.temenos.responder.flows.VersionInformationFlow
import com.temenos.responder.context.builder.ExecutionContextBuilder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by dgroves on 17/01/2017.
 */
class FlowDispatcherTest extends Specification {

    @Unroll
    def "Create and execute an instance of Flow class #flowClass.simpleName"(Class flowClass) {
        given:
            def mockExecutor = Mock(FlowExecutor)
            def requestContext = Mock(RequestContext)
            def executionContext = Mock(ExecutionContext)
            def factory = Mock(ContextBuilderFactory)
            def contextBuilder = Mock(ExecutionContextBuilder)
            def manager = Mock(ContextManager)
            def dispatcher = new FlowDispatcher(mockExecutor, requestContext, manager, factory)
            def parameters = Mock(Parameters)
            def links = Mock(Entity), body = Mock(Entity)
            def crossFlowContext = Mock(CrossFlowContext)
        when:
            def result = dispatcher.notify(flowClass, 1)
        then:
            factory.getExecutionContextBuilder(manager) >> contextBuilder
            manager.manageContext(_) >> 2
            contextBuilder.flow(_) >> contextBuilder
            contextBuilder.serverRoot(_) >> contextBuilder
            contextBuilder.origin(_) >> contextBuilder
            contextBuilder.dispatcher(_) >> contextBuilder
            contextBuilder.requestBody(_) >> contextBuilder
            contextBuilder.requestParameters(_) >> contextBuilder
            contextBuilder.resourceName(_) >> contextBuilder
            contextBuilder.build() >> executionContext
            contextBuilder.buildAndGetId() >> 2
            parameters.getParameterKeys() >> ['alpha', 'beta', 'gamma']
            parameters.getValue('alpha') >> 'a'
            parameters.getValue('beta') >> 'b'
            parameters.getValue('gamma') >> 'c'
            crossFlowContext.parameters() >> parameters

            1 * executionContext.getAttribute("document.links.self") >> links
            1 * executionContext.getAttribute("finalResult") >> body
            1 * manager.getManagedContext(1, CrossFlowContext) >> crossFlowContext
            1 * manager.getManagedContext(2, ExecutionContext) >> executionContext
            1 * links.getValues() >> [:]
            1 * body.getValues() >> ["Greeting": "Hello", "Subject": "World"]
            1 * mockExecutor.execute({ flowClass.isAssignableFrom(it.class) }, _)

            requestContext.getResourceName() >> "helloWorld"
            result instanceof Document
        where:
            flowClass << [AdditionFlow]
    }

    @Unroll
    def "Create and execute instances of Flow classes #flowClasses.simpleName in parallel"(List flowClasses, String[] into) {
        given:
            def mockExecutor = Mock(FlowExecutor)
            def requestContext = Mock(RequestContext)
            def executionContext = Mock(ExecutionContext)
            def contextBuilder = Mock(ExecutionContextBuilder)
            def manager = Mock(ContextManager)
            def factory = Mock(ContextBuilderFactory)
            def dispatcher = new FlowDispatcher(mockExecutor, requestContext, manager, factory)
            def parameters = Mock(Parameters)
            def links = Mock(Entity), body = Mock(Entity)
            def crossFlowContext = Mock(CrossFlowContext)

        when:
            def result = dispatcher.notify(flowClasses, 1, into)
        then:
            factory.getExecutionContextBuilder(manager) >> contextBuilder
            contextBuilder.flow(_) >> contextBuilder
            contextBuilder.serverRoot(_) >> contextBuilder
            contextBuilder.origin(_) >> contextBuilder
            contextBuilder.dispatcher(_) >> contextBuilder
            contextBuilder.requestBody(_) >> contextBuilder
            contextBuilder.requestParameters(_) >> contextBuilder
            contextBuilder.resourceName(_) >> contextBuilder
            contextBuilder.build() >> executionContext
            contextBuilder.buildAndGetId() >> 2
            crossFlowContext.into() >> into
            parameters.getParameterKeys() >> ['alpha', 'beta', 'gamma']
            parameters.getValue('alpha') >> 'a'
            parameters.getValue('beta') >> 'b'
            parameters.getValue('gamma') >> 'c'
            crossFlowContext.parameters() >> parameters

            manager.manageContext(_) >> 1
            manager.getManagedContext(1, CrossFlowContext) >> crossFlowContext
            manager.getManagedContext(2, ExecutionContext) >> executionContext

            (flowClasses.size()) * executionContext.getAttribute("document.links.self") >> links
            (flowClasses.size()) * executionContext.getAttribute('finalResult') >> body
            (flowClasses.size()) * mockExecutor.execute(_, _)

            links.getValues() >> [:]
            body.getValues() >> ["Greeting": "Hello", "Subject": "World"]
            requestContext.parameters() >> parameters
            requestContext.getResourceName() >> "helloWorld"
            flowClasses.collect { Class flowClass -> result.collect { key, val -> key == flowClass.simpleName } }.size() == flowClasses.size()
        where:
            flowClasses                                | into
            [AdditionFlow, AdditionFlow, AdditionFlow] | ['a', 'b', 'c']
            [VersionInformationFlow, AdditionFlow]     | ['a', 'b']
            [CustomerInformation]                      | ['a']
    }
}
