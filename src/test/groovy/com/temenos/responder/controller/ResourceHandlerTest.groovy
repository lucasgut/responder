package com.temenos.responder.controller

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.temenos.responder.configuration.ResourceSpec
import com.temenos.responder.exception.MethodNotFoundException
import com.temenos.responder.exception.VersionNotFoundException
import com.temenos.responder.mapper.ResourceBuilder
import spock.lang.Specification

/**
 * Created by aburgos on 13/01/2017.
 */
class ResourceHandlerTest extends Specification {

    def "Fetch #methodName version #versionName "(methodName, versionName, directives) {
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

    def "Getting nonexistent method"(methodName, versionName, directives) {
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
        then:
            thrown(MethodNotFoundException)
        where:
            methodName | versionName | directives
            "POST"     | "default"   | ["GET": ["routeTo": ["default": ["flow": "com.temenos.responder.flows.AdditionFlow"]]]]
            "DELETE"   | "2.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]]]
            "PATCH"    | "2.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow", "response": ["description": "Account Dashboard response", "item": "savingsAPI.accounts/AccountDashboard"]], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow", "request": ["description": "Account Dashboard request", "item": "savingsAPI.accounts/AccountDashboard"]]]]]
    }

    def "Getting nonexistent version"(methodName, versionName, directives) {
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
            thrown(VersionNotFoundException)
        where:
            methodName | versionName | directives
            "GET"      | "1.0"       | ["GET": ["routeTo": ["default": ["flow": "com.temenos.responder.flows.AdditionFlow"]]]]
            "GET"      | "3.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]]]
            "POST"     | "4.0"       | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow", "response": ["description": "Account Dashboard response", "item": "savingsAPI.accounts/AccountDashboard"]], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow", "request": ["description": "Account Dashboard request", "item": "savingsAPI.accounts/AccountDashboard"]]]]]
    }
}