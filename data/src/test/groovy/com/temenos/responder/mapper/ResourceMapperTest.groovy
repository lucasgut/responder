package com.temenos.responder.mapper

import com.temenos.responder.commands.ScaffoldAddition
import com.temenos.responder.commands.ScaffoldVersion
import com.temenos.responder.configuration.Resource
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 21/12/2016.
 */
class ResourceMapperTest extends Specification {

    def "Map a definition containing a single resource with one directive to a list of Resources"(resources, pathSpecs, names, modelSpecs, scopes, httpMethods) {
        given:
            def resourceMapper = new ResourceMapper()
        when:
            def result = resourceMapper.map(resources) as List<Resource>
        then:
            result.size() == resources.resources.size()
            result*.pathSpec == pathSpecs
            result*.nameSpec == names
            result*.httpMethod == httpMethods
            result*.modelSpec == modelSpecs
            result*.scope == scopes
        where:
            resources                                                                                                                                                                                                                                          | pathSpecs   | names          | modelSpecs                 | scopes     | httpMethods
            ["resources": [["appVersion": ["scope": "public", "path": "version", "directive": ["GET": ["workflow": "com.temenos.responder.commands.VersionInformation", "responses": ["200": ["item": "com.temenos.responder.commands.ScaffoldVersion"]]]]]]]] | ['version'] | ['appVersion'] | [['200': ScaffoldVersion]] | ['public'] | ['GET']
    }

    def "Map a definition containing multiple resources with one directive to a list of Resources"(resources, pathSpecs, names, modelSpecs, scopes, httpMethods) {
        given:
            def resourceMapper = new ResourceMapper()
        when:
            def result = resourceMapper.map(resources) as List<Resource>
        then:
            result.size() == resources.resources.size()
            result*.pathSpec == pathSpecs
            result*.nameSpec == names
            result*.httpMethod == httpMethods
            result*.modelSpec == modelSpecs
            result*.scope == scopes
        where:
            resources                                                                                                                                                                                                                                                                                                                                                                                                                                                                   | pathSpecs          | names                 | modelSpecs                                            | scopes               | httpMethods
            ["resources": [["appVersion": ["scope": "public", "path": "version", "directive": ["GET": ["workflow": "com.temenos.responder.commands.VersionInformation", "responses": ["200": ["item": "com.temenos.responder.commands.ScaffoldVersion"]]]]]], ["add": ["scope": "public", "path": "add", "directive": ["GET": ["workflow": "com.temenos.responder.commands.VersionInformation", "responses": ["200": ["item": "com.temenos.responder.commands.ScaffoldAddition"]]]]]]]] | ['version', 'add'] | ['appVersion', 'add'] | [['200': ScaffoldVersion], ['200': ScaffoldAddition]] | ['public', 'public'] | ['GET', 'GET']
    }

    def "Map a definition containing a single resource with multiple response codes to a list of Resources"(resources, pathSpecs, names, modelSpecs, scopes, httpMethods) {
        given:
            def resourceMapper = new ResourceMapper()
        when:
            def result = resourceMapper.map(resources) as List<Resource>
        then:
            result.size() == resources.resources.size()
            result*.pathSpec == pathSpecs
            result*.nameSpec == names
            result*.httpMethod == httpMethods
            result*.modelSpec == modelSpecs
            result*.scope == scopes
        where:
            resources                                                                                                                                                                                                                                                                                                              | pathSpecs   | names          | modelSpecs                                          | scopes     | httpMethods
            ["resources": [["appVersion": ["scope": "public", "path": "version", "directive": ["GET": ["workflow": "com.temenos.responder.commands.VersionInformation", "responses": ["200": ["item": "com.temenos.responder.commands.ScaffoldVersion"], "404": ["item": "com.temenos.responder.commands.ScaffoldAddition"]]]]]]]] | ['version'] | ['appVersion'] | [['200': ScaffoldVersion, '404': ScaffoldAddition]] | ['public'] | ['GET']
    }

    def "Map a definition containing a single resource with multiple method bindings to a list of Resources"(resources, pathSpecs, names, modelSpecs, scopes, expectedSize, httpMethods) {
        given:
            def resourceMapper = new ResourceMapper()
        when:
            def result = resourceMapper.map(resources) as List<Resource>
        then:
            result.size() == expectedSize
            result*.pathSpec == pathSpecs
            result*.nameSpec == names
            result*.httpMethod == httpMethods
            result*.modelSpec == modelSpecs
            result*.scope == scopes
        where:
            resources                                                                                                                                                                                                                                                                                                                                                                                                        | pathSpecs              | names                        | modelSpecs                                            | scopes               | expectedSize | httpMethods
            ["resources": [["appVersion": ["scope": "public", "path": "version", "directive": ["GET": ["workflow": "com.temenos.responder.commands.VersionInformation", "responses": ["200": ["item": "com.temenos.responder.commands.ScaffoldVersion"]]], "POST": ["workflow": "com.temenos.responder.commands.VersionInformation", "responses": ["200": ["item": "com.temenos.responder.commands.ScaffoldAddition"]]]]]]]] | ['version', 'version'] | ['appVersion', 'appVersion'] | [['200': ScaffoldVersion], ['200': ScaffoldAddition]] | ['public', 'public'] | 2            | ['GET', 'POST']
    }
}
