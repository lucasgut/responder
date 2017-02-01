package com.temenos.responder.mapper

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.JsonNodeFactory
import com.temenos.responder.configuration.Method
import com.temenos.responder.configuration.Resource
import com.temenos.responder.configuration.ResourceSpec
import com.temenos.responder.configuration.Version
import jdk.management.resource.ResourceType
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 10/01/2017.
 */
class ResourceBuilderTest extends Specification {

    @Unroll
    def "Build #name resource #description to #path"(name, path, parameters, methods, description) {
        given:
            def parser = new ResourceBuilder()
            def nodeFactory = JsonNodeFactory.instance;
            def childNode = nodeFactory.objectNode();
            childNode.put(ResourceSpec.PATH, path)
            ObjectMapper mapper = new ObjectMapper()
            JsonNode parametersNode = mapper.valueToTree(parameters)
            childNode.put(ResourceSpec.PARAMETERS, parametersNode)
            JsonNode methodsNode = mapper.valueToTree(methods)
            childNode.put(ResourceSpec.DIRECTIVES, methodsNode)
            def node = nodeFactory.objectNode();
            node.put(name, childNode)
        when:
            def resource = parser.getResource(node);
        then:
            resource.getName() == name
            resource.getPath() == path
            resource.getParameters().forEach { parameter ->
                def parameterName = parameter.getName()
                assert parameterName != null
                def expectedParameter = parameters.get(parameterName)
                assert parameter.getType() == expectedParameter.get(ResourceSpec.PARAMETER_TYPE)
                assert parameter.getDescription() == expectedParameter.get(ResourceSpec.PARAMETER_DESCRIPTION)
                assert parameter.getIn().value == expectedParameter.get(ResourceSpec.PARAMETER_IN)
            }
            resource.getDirectives().forEach { resourceMethod ->
                def methodName = resourceMethod.method.name()
                assert methods[methodName] != null
                def versions = methods[methodName][ResourceSpec.ROUTE_TO]
                assert versions != null
                def resourceVersions = resourceMethod.getVersions()
                resourceVersions.forEach({ resourceVersion ->
                    def versionName = resourceVersion.getName()
                    assert versions[versionName] != null
                    def version = versions[versionName]
                    resourceVersion.getFlow().name == version[ResourceSpec.FLOW]
                })
            }
        where:
            name        | path                | parameters                                                                                             | methods                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | description
            "dashboard" | '/accountDashboard' | ["truncate": ["type": "boolean", "description": "truncate addition result to integer", "in": "query"]] | ["GET": ["routeTo": ["default": ["flow": "com.temenos.responder.flows.AdditionFlow"]]]]                                                                                                                                                                                                                                                                                                                                                                                                                                                   | 'returning an AdditionFlow if a GET request is made'
            "dashboard" | '/accountDashboard' | []                                                                                                     | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]]]                                                                                                                                                                                                                     | 'returning an AdditionFlow or a VersionInformationFlow if a GET request is made with Accept-Version set to 1.0 or 2.0'
            "dashboard" | '/accountDashboard' | ["truncate": ["type": "boolean", "description": "truncate addition result to integer", "in": "path"]]  | ["GET": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow", "response": ["description": "Account Dashboard response", "item": "savingsAPI.accounts/AccountDashboard"]], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow"]]], "POST": ["routeTo": ["1.0": ["flow": "com.temenos.responder.flows.AdditionFlow"], "2.0": ["flow": "com.temenos.responder.flows.VersionInformationFlow", "request": ["description": "Account Dashboard request", "item": "savingsAPI.accounts/AccountDashboard"]]]]] | 'returning an AdditionFlow or VersionInformationFlow if a GET or POST request is made with Accept-Version set to 1.0 or 2.0'
    }
}