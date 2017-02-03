package com.temenos.responder.context

import com.temenos.responder.context.builder.ContextBuilderFactory
import com.temenos.responder.context.builder.CrossFlowContextBuilder
import com.temenos.responder.context.manager.ContextManager
import com.temenos.responder.dispatcher.Dispatcher
import com.temenos.responder.entity.runtime.Document
import com.temenos.responder.flows.CustomerAddressFlow
import com.temenos.responder.flows.CustomerInformation
import com.temenos.responder.flows.Flow
import com.temenos.responder.flows.ParallelTestFlow
import com.temenos.responder.flows.VersionInformationFlow
import spock.lang.Specification
import spock.lang.Unroll;

/**
 * Created by dgroves on 31/01/2017.
 */
class DefaultExecutionContextTest extends Specification {

    @Unroll
    def "Notify dispatchers that #flow.simpleName is ready to execute with parameters and map the result to #into"(Class<Flow> flow, String into) {
        given:
            def serverRoot = 'http://0.0.0.0/root'
            def self = 'http://0.0.0.0/root/resource'
            def resourceName = 'Resource'
            def dispatcher = Mock(Dispatcher)
            def factory = Mock(ContextBuilderFactory)
            def manager = Mock(ContextManager)
            def contextAttributes = [:]
            def executionContext = new DefaultExecutionContext(serverRoot, self, resourceName, null, contextAttributes, null, dispatcher, factory, manager, CustomerInformation)
            def responseData = Mock(Document)
            def builder = Mock(CrossFlowContextBuilder)
            def context = Mock(CrossFlowContext)
            def from = Mock(Parameters)
        when:
            executionContext.executeFlow(flow, from, into)
        then:
            contextAttributes[into] == responseData
            builder.origin(_, _, _) >> builder
            builder.parameters(_) >> builder
            builder.into('flowResult') >> builder
            builder.buildAndGetId() >> 1
            factory.getCrossFlowContextBuilder(manager) >> builder

            1 * dispatcher.notify(flow, 1) >> responseData
        where:
            flow                | into
            CustomerAddressFlow | 'flowResult'
    }

    @Unroll
    def "Notify dispatchers that #flows.simpleName are ready to execute in parallel with parameters and map the results to #into"(flows, into) {
        given: "A parent flow executed from a resource"
            def serverRoot = 'http://0.0.0.0/root'
            def self = 'http://0.0.0.0/root/resource'
            def resourceName = 'Resource'
            def dispatcher = Mock(Dispatcher)
            def manager = Mock(ContextManager)
            def contextAttributes = [:]
            def responseData = into.collectEntries { param -> [(param): Mock(Document)] }
        and: "A managed context builder that returns a managed cross flow context ID number of 1"
            def builder = Mock(CrossFlowContextBuilder){ builder ->
                builder.origin(_, _, _) >> builder
                builder.parameters(_) >> builder
                builder.into(into) >> builder
                builder.buildAndGetId() >> 1
            }
        and: "A factory that returns a manged cross flow context builder"
            def factory = Mock(ContextBuilderFactory){ factory ->
                factory.getCrossFlowContextBuilder(manager) >> builder
            }
            def context = Mock(CrossFlowContext)
            def from = [Mock(Parameters)]
            def executionContext = new DefaultExecutionContext(serverRoot, self, resourceName, null, contextAttributes, null, dispatcher, factory, manager, ParallelTestFlow)
        when: "A list of flows are made available to the dispatcher"
            executionContext.executeFlows(flows, from, into)
        then: "The result of each flow must be mapped to a respective context attribute name"
            contextAttributes == responseData
        and: "The dispatcher will execute the given flows and will map each result to each parameter name"
            1 * dispatcher.notify(flows, 1) >> responseData
        where:
            flows                                                              | into
            [VersionInformationFlow, CustomerAddressFlow, CustomerInformation] | ['versionResult', 'addressResult', 'customerInfoResult']
            [VersionInformationFlow]                                           | ['flowResult']
    }
}
