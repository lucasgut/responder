package com.temenos.responder.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.temenos.responder.configuration.ResourceSpec
import com.temenos.responder.exception.MethodNotFoundException
import com.temenos.responder.exception.VersionNotFoundException
import com.temenos.responder.mapper.ResourceBuilder
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 13/01/2017.
 */
class ResourceHandlerTest extends Specification {

    @Unroll
    def "Fetch model for method '#methodName' and \
        model for version '#versionName'"(methodName, versionName, directives) {
        given:
            def parser = new ResourceBuilder()
            def nodeFactory = JsonNodeFactory.instance;
            def childNode = nodeFactory.objectNode()
            childNode.put(ResourceSpec.PATH, "/path")
            ObjectMapper mapper = new ObjectMapper()
            JsonNode methodsNode = mapper.valueToTree(directives)
            childNode.set(ResourceSpec.DIRECTIVES, methodsNode)
            def node = nodeFactory.objectNode()
            node.set("anonymous", childNode)
            def resource = parser.getResource(node)
            def handler = new ResourceHandler()
        when:
            def method = handler.getMethod(resource, methodName)
            def version = handler.getVersion(resource, methodName, versionName)
        then:
            method.getMethod().name() == methodName
            def versions = directives[methodName][ResourceSpec.ROUTE_TO]
            assert versions != null
            assert version.getName() == versionName
        where:
            methodName | versionName | directives
            "GET"      | "default"   | ["GET": ["routeTo": ["default": ["flow": "com.temenos.responder.flows.AdditionFlow"]]]]
            "GET"      | "2.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]]]
            "POST"     | "2.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow", "response": ["description": "Account Dashboard response", "item": "savingsAPI.accounts/AccountDashboard"]], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow", "request": ["description": "Account Dashboard request", "item": "savingsAPI.accounts/AccountDashboard"]]]]]
    }

    @Unroll
    def "Throw #exception.simpleName if #description doesn't \
        exist in the chosen resource definition"(exception, description, methodName, versionName, directives) {
        given:
            def parser = new ResourceBuilder()
            def nodeFactory = JsonNodeFactory.instance;
            def childNode = nodeFactory.objectNode()
            childNode.put(ResourceSpec.PATH, "/path")
            ObjectMapper mapper = new ObjectMapper()
            JsonNode methodsNode = mapper.valueToTree(directives)
            childNode.set(ResourceSpec.DIRECTIVES, methodsNode)
            def node = nodeFactory.objectNode()
            node.set("anonymous", childNode)
            def resource = parser.getResource(node)
            def handler = new ResourceHandler()
        when:
            handler.getMethod(resource, methodName)
            handler.getVersion(resource, methodName, versionName)
        then:
            thrown(exception)
        where:
            exception                | description     | methodName | versionName | directives
            MethodNotFoundException  | 'method POST'   | "POST"     | "default"   | ["GET": ["routeTo": ["default": ["flow": "com.temenos.responder.flows.AdditionFlow"]]]]
            MethodNotFoundException  | 'method DELETE' | "DELETE"   | "2.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]]]
            MethodNotFoundException  | 'method PATCH'  | "PATCH"    | "2.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow", "response": ["description": "Account Dashboard response", "item": "savingsAPI.accounts/AccountDashboard"]], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow", "request": ["description": "Account Dashboard request", "item": "savingsAPI.accounts/AccountDashboard"]]]]]
            VersionNotFoundException | 'version 1.0'   | "GET"      | "1.0"       | ["GET": ["routeTo": ["default": ["flow": "com.temenos.responder.flows.AdditionFlow"]]]]
            VersionNotFoundException | 'version 3.0'   | "GET"      | "3.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]]]
            VersionNotFoundException | 'version 4.0'   | "POST"     | "4.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow", "response": ["description": "Account Dashboard response", "item": "savingsAPI.accounts/AccountDashboard"]], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow", "request": ["description": "Account Dashboard request", "item": "savingsAPI.accounts/AccountDashboard"]]]]]
    }
}