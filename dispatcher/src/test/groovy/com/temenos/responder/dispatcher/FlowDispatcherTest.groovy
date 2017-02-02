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
import com.temenos.responder.flows.VersionInformationFlow
import com.temenos.responder.context.builder.ExecutionContextBuilder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by dgroves on 17/01/2017.
 */
class FlowDispatcherTest extends Specification {

    @Unroll
    def "Create and execute an instance of Flow class #flowClass.simpleName from a Resource"(Class flowClass){
        given: "Resource 'helloWorld' triggered the flow"
            def requestContext = Mock(RequestContext){ RequestContext req ->
                req.getResourceName() >> "helloWorld"
                req.parameters() >> Mock(Parameters)
            }
        and: "Context builder returns the next available context ID when a new execution context is created"
            def mockExecutor = Mock(FlowExecutor)
            def executionContext = Mock(ExecutionContext)
            def contextBuilder = Mock(ExecutionContextBuilder){ contextBuilder ->
                contextBuilder._(_) >> contextBuilder
                contextBuilder.build() >> executionContext
                contextBuilder.buildAndGetId() >> 1
            }
            def links = Mock(Entity), body = Mock(Entity)
            def manager = Mock(ContextManager){ manager ->
                manager.manageContext(_) >> 1
            }
        and: "Context builder factory returns a managed execution context builder"
            def factory = Mock(ContextBuilderFactory){ factory ->
                factory.getExecutionContextBuilder(manager) >> contextBuilder
            }
            def dispatcher = new FlowDispatcher(mockExecutor, requestContext, manager, factory)
        when: "The flow is executed"
            def result = dispatcher.notify(flowClass)
        then: "The result of the operation should be a rendered document containing links, embedded data and a body"
            result instanceof Document
        and: "The result of the operation should be mapped to the 'finalResult' attribute name"
            1 * executionContext.getAttribute("finalResult") >> body
        and: 'Any outgoing data should be encapsulated in an Entity instance'
            1 * body.getValues() >> ["Greeting": "Hello", "Subject": "World"]
        and: "Links should be present in the body"
            1 * executionContext.getAttribute("document.links.self") >> links
            1 * links.getValues() >> [:]
        and: "All context instances should be managed by the context manager"
            1 * manager.getManagedContext(1, ExecutionContext) >> executionContext
        and: "The created flow instance should be executed by the flow executor"
            1 * mockExecutor.execute({ flowClass.isAssignableFrom(it.class) }, _)
        where:
            flowClass << [VersionInformationFlow]
    }

    @Unroll
    def "Create and execute an instance of Flow class #flowClass.simpleName from a Flow"(Class flowClass) {
        given: "Resource 'helloWorld' triggered the parent flow"
            def requestContext = Mock(RequestContext){ requestContext ->
                requestContext.getResourceName() >> "helloWorld"
            }
        and: "Context builder returns the next available context ID when a new execution context is created"
            def mockExecutor = Mock(FlowExecutor)
            def executionContext = Mock(ExecutionContext)
            def contextBuilder = Mock(ExecutionContextBuilder){ contextBuilder ->
                contextBuilder._(_) >> contextBuilder
                contextBuilder.build() >> executionContext
                contextBuilder.buildAndGetId() >> 2
            }
            def links = Mock(Entity), body = Mock(Entity)
            def manager = Mock(ContextManager){ manager ->
                manager.manageContext(_) >> 2
            }
        and: "Context builder factory returns a managed execution context builder"
            def factory = Mock(ContextBuilderFactory){ factory ->
                factory.getExecutionContextBuilder(manager) >> contextBuilder
            }
        and: "The flow was triggered by another flow"
            def parameters = Mock(Parameters)
            def crossFlowContext = Mock(CrossFlowContext){ crossFlowContext ->
                crossFlowContext.parameters() >> parameters
            }
            def dispatcher = new FlowDispatcher(mockExecutor, requestContext, manager, factory)
        when: "The flow is executed"
            def result = dispatcher.notify(flowClass, 1)
        then: "The result of the operation should be a rendered document containing links, embedded data and a body"
            result instanceof Document
        and: "The result of the operation should be mapped to the 'finalResult' attribute name"
            1 * executionContext.getAttribute("finalResult") >> body
        and: 'Any outgoing data should be encapsulated in an Entity instance'
            1 * body.getValues() >> ["Greeting": "Hello", "Subject": "World"]
        and: "Links should be present in the body"
            1 * executionContext.getAttribute("document.links.self") >> links
            1 * links.getValues() >> [:]
        and: "All context instances should be managed by the context manager"
            1 * manager.getManagedContext(1, CrossFlowContext) >> crossFlowContext
            1 * manager.getManagedContext(2, ExecutionContext) >> executionContext
        and: "The created flow instance should be executed by the flow executor"
            1 * mockExecutor.execute({ flowClass.isAssignableFrom(it.class) }, _)
        where:
            flowClass << [AdditionFlow]
    }

    @Unroll
    def "Create and execute instances of Flow classes #flowClasses.simpleName in parallel"(flowClasses, into) {
        given:
            def requestContext = Mock(RequestContext){ requestContext ->
                requestContext.getResourceName() >> "helloWorld"
            }
            def mockExecutor = Mock(FlowExecutor)
            def executionContext = Mock(ExecutionContext)
            def contextBuilder = Mock(ExecutionContextBuilder){ contextBuilder ->
                contextBuilder._(_) >> contextBuilder
                contextBuilder.build() >> executionContext
                contextBuilder.buildAndGetId() >> 2
            }
            def manager = Mock(ContextManager)
            def factory = Mock(ContextBuilderFactory)
            def dispatcher = new FlowDispatcher(mockExecutor, requestContext, manager, factory)
            def links = Mock(Entity), body = Mock(Entity)
            def crossFlowContext = Mock(CrossFlowContext)

        when:
            def result = dispatcher.notify(flowClasses, 1)
        then:
            factory.getExecutionContextBuilder(manager) >> contextBuilder
            crossFlowContext.into() >> into
            crossFlowContext.allParameters() >> flowClasses.collect { Mock(Parameters) }

            manager.manageContext(_) >> 1
            manager.getManagedContext(1, CrossFlowContext) >> crossFlowContext
            manager.getManagedContext(2, ExecutionContext) >> executionContext

            (flowClasses.size()) * executionContext.getAttribute("document.links.self") >> links
            (flowClasses.size()) * executionContext.getAttribute('finalResult') >> body
            (flowClasses.size()) * mockExecutor.execute(_, _)

            links.getValues() >> [:]
            body.getValues() >> ["Greeting": "Hello", "Subject": "World"]
            requestContext.getResourceName() >> "helloWorld"
            flowClasses.collect { Class flowClass -> result.collect { key, val -> key == flowClass.simpleName } }.size() == flowClasses.size()
        where:
            flowClasses                                | into
            [AdditionFlow, AdditionFlow, AdditionFlow] | ['a', 'b', 'c']
            [VersionInformationFlow, AdditionFlow]     | ['a', 'b']
            [CustomerInformation]                      | ['a']
    }
}
