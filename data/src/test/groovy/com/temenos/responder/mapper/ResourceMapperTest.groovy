package com.temenos.responder.mapper

import com.temenos.responder.scaffold.ScaffoldAdditionInput
import com.temenos.responder.scaffold.ScaffoldAdditionOutput
import com.temenos.responder.scaffold.ScaffoldVersion
import com.temenos.responder.configuration.Resource
import spock.lang.Specification

/**
 * Created by Douglas Groves on 21/12/2016.
 */
class ResourceMapperTest extends Specification {

    def "Map a definition containing a single resource with one directive to a list of Resources"(resources, pathSpecs, names, outputModelSpecs, inputModelSpec, scopes, httpMethods) {
        given:
            def resourceMapper = new ResourceMapper()
        when:
            def result = resourceMapper.map(resources) as List<Resource>
        then:
            result.size() == resources.resources.size()
            result*.pathSpec == pathSpecs
            result*.nameSpec == names
            result*.httpMethod == httpMethods
            result*.outputModelSpec == outputModelSpecs
            result*.inputModelSpec == inputModelSpec
            result*.scope == scopes
        where:
            resources                                                                                                                                                                                                                                          | pathSpecs   | names          | outputModelSpecs           | inputModelSpec | scopes     | httpMethods
            ["resources": [["appVersion": ["scope": "public", "path": "version", "directive": ["GET": ["workflow": "com.temenos.responder.flows.VersionInformationFlow", "responses": ["200": ["item": "com.temenos.responder.scaffold.ScaffoldVersion"]]]]]]]] | ['version'] | ['appVersion'] | [['200': ScaffoldVersion]] | [null]         | ['public'] | ['GET']
    }

    def "Map a definition containing multiple resources with one directive to a list of Resources"(resources, pathSpecs, names, outputModelSpecs, inputModelSpec, scopes, httpMethods) {
        given:
            def resourceMapper = new ResourceMapper()
        when:
            def result = resourceMapper.map(resources) as List<Resource>
        then:
            result.size() == resources.resources.size()
            result*.pathSpec == pathSpecs
            result*.nameSpec == names
            result*.httpMethod == httpMethods
            result*.inputModelSpec == inputModelSpec
            result*.outputModelSpec == outputModelSpecs
            result*.scope == scopes
        where:
            resources                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | pathSpecs          | names                 | outputModelSpecs                                            | inputModelSpec                | scopes               | httpMethods
            ["resources": [["appVersion": ["scope": "public", "path": "version", "directive": ["GET": ["workflow": "com.temenos.responder.flows.VersionInformationFlow", "responses": ["200": ["item": "com.temenos.responder.scaffold.ScaffoldVersion"]]]]]], ["add": ["scope": "public", "path": "add", "directive": ["GET": ["workflow": "com.temenos.responder.flows.VersionInformationFlow", "item": "com.temenos.responder.scaffold.ScaffoldAdditionInput", "responses": ["200": ["item": "com.temenos.responder.scaffold.ScaffoldAdditionOutput"]]]]]]]] | ['version', 'add'] | ['appVersion', 'add'] | [['200': ScaffoldVersion], ['200': ScaffoldAdditionOutput]] | [null, ScaffoldAdditionInput] | ['public', 'public'] | ['GET', 'GET']
    }

    def "Map a definition containing a single resource with multiple response codes to a list of Resources"(resources, pathSpecs, names, outputModelSpecs, inputModelSpec, scopes, httpMethods) {
        given:
            def resourceMapper = new ResourceMapper()
        when:
            def result = resourceMapper.map(resources) as List<Resource>
        then:
            result.size() == resources.resources.size()
            result*.pathSpec == pathSpecs
            result*.nameSpec == names
            result*.httpMethod == httpMethods
            result*.inputModelSpec == inputModelSpec
            result*.outputModelSpec == outputModelSpecs
            result*.scope == scopes
        where:
            resources                                                                                                                                                                                                                                                                                                                    | pathSpecs   | names          | outputModelSpecs                                          | inputModelSpec | scopes     | httpMethods
            ["resources": [["appVersion": ["scope": "public", "path": "version", "directive": ["GET": ["workflow": "com.temenos.responder.flows.VersionInformationFlow", "responses": ["200": ["item": "com.temenos.responder.scaffold.ScaffoldVersion"], "404": ["item": "com.temenos.responder.scaffold.ScaffoldAdditionOutput"]]]]]]]] | ['version'] | ['appVersion'] | [['200': ScaffoldVersion, '404': ScaffoldAdditionOutput]] | [null]         | ['public'] | ['GET']
    }

    def "Map a definition containing a single resource with multiple method bindings to a list of Resources"(resources, pathSpecs, names, outputModelSpecs, inputModelSpec, scopes, expectedSize, httpMethods) {
        given:
            def resourceMapper = new ResourceMapper()
        when:
            def result = resourceMapper.map(resources) as List<Resource>
        then:
            result.size() == expectedSize
            result*.pathSpec == pathSpecs
            result*.nameSpec == names
            result*.httpMethod == httpMethods
            result*.outputModelSpec == outputModelSpecs
            result*.inputModelSpec == inputModelSpec
            result*.scope == scopes
        where:
            resources                                                                                                                                                                                                                                                                                                                                                                                                                                                                              | pathSpecs              | names                        | outputModelSpecs                                            | inputModelSpec                | scopes               | expectedSize | httpMethods
            ["resources": [["appVersion": ["scope": "public", "path": "version", "directive": ["GET": ["workflow": "com.temenos.responder.flows.VersionInformationFlow", "responses": ["200": ["item": "com.temenos.responder.scaffold.ScaffoldVersion"]]], "POST": ["workflow": "com.temenos.responder.flows.VersionInformationFlow", "item": "com.temenos.responder.scaffold.ScaffoldAdditionInput", "responses": ["200": ["item": "com.temenos.responder.scaffold.ScaffoldAdditionOutput"]]]]]]]] | ['version', 'version'] | ['appVersion', 'appVersion'] | [['200': ScaffoldVersion], ['200': ScaffoldAdditionOutput]] | [null, ScaffoldAdditionInput] | ['public', 'public'] | 2            | ['GET', 'POST']
    }
}
