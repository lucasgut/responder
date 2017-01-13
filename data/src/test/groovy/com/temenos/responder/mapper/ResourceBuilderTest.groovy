package com.temenos.responder.mapper

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.temenos.responder.configuration.Method
import com.temenos.responder.configuration.Resource
import com.temenos.responder.configuration.ResourceSpec
import com.temenos.responder.configuration.Version
import spock.lang.Specification

/**
 * Created by aburgos on 10/01/2017.
 */
class ResourceBuilderTest extends Specification {

    def "Build resource"(name, path, methods) {
        given:
            def parser = new ResourceBuilder()
            def nodeFactory = JsonNodeFactory.instance;
            def childNode = nodeFactory.objectNode();
            childNode.put(ResourceSpec.PATH, path)
            ObjectMapper mapper = new ObjectMapper()
            JsonNode methodsNode = mapper.valueToTree(methods)
            childNode.put(ResourceSpec.DIRECTIVES, methodsNode)
            def node = nodeFactory.objectNode();
            node.put(name, childNode)
        when:
            def resource = parser.getResource(node);
        then:
            resource.getName() == name
            resource.getPath() == path
            resource.getDirectives().forEach { resourceMethod ->
                def methodName = resourceMethod.method.name()
                assert methods[methodName] != null
                def versions = methods[methodName][ResourceSpec.ROUTE_TO]
                assert versions != null
                def resourceVersions = resourceMethod.getVersions()
                resourceVersions.forEach ({ resourceVersion ->
                    def versionName = resourceVersion.getName()
                    assert versions[versionName] != null
                    def version = versions[versionName]
                    resourceVersion.getFlow().getClass().name == version[ResourceSpec.FLOW]
                })
            }
        where:
            name        | path                | methods
            "dashboard" | '/accountDashboard' | ["GET": ["routeTo": ["default": ["flow": "com.temenos.responder.flows.AdditionFlow"]]]]
            "dashboard" | '/accountDashboard' | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]]]
            "dashboard" | '/accountDashboard' | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow", "response": ["description": "Account Dashboard response", "item": "savingsAPI.accounts/AccountDashboard" ]], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow", "request": ["description": "Account Dashboard request", "item": "savingsAPI.accounts/AccountDashboard" ]]]]]
    }
}