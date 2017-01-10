package com.temenos.responder.paths

import com.temenos.responder.configuration.Resource
import com.temenos.responder.exception.ResourceNotFoundException
import com.temenos.responder.loader.ScriptLoader
import spock.lang.Specification
import spock.lang.Unroll;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class ResourcePathHandlerTest extends Specification {

    def resources

    def setup() {
        def mockCollectionResource = Mock(Resource),
            mockItemResource = Mock(Resource),
            mockAnotherItemResource = Mock(Resource),
            mockItemWithItemResource = Mock(Resource)
        def mockScriptLoader = Mock(ScriptLoader)
        _ * mockCollectionResource.getPathSpec() >> 'tests'
        _ * mockCollectionResource.getHttpMethod() >> "GET"
        _ * mockItemResource.getPathSpec() >> 'tests/{id}'
        _ * mockItemResource.getHttpMethod() >> "GET"
        _ * mockAnotherItemResource.getPathSpec() >> 'tests/{id}'
        _ * mockAnotherItemResource.getHttpMethod() >> "POST"
        _ * mockItemWithItemResource.getPathSpec() >> 'tests/{TestId}/history/{HistoryId}'
        _ * mockItemWithItemResource.getHttpMethod() >> "GET"
        resources = [mockCollectionResource, mockItemResource, mockAnotherItemResource, mockItemWithItemResource]
    }

    @Unroll
    def "Resolve #spec if path #path is requested"(path, spec, method) {
        setup:
            def pathHandler = new ResourcePathHandler(resources)
        when:
            def result = pathHandler.resolvePathSpecification(path, method)
        then:
            result.pathSpec == spec
            result.httpMethod == method
        where:
            path                 | spec                                 | method
            'tests'              | 'tests'                              | 'GET'
            'tests/'             | 'tests'                              | 'GET'
            'tests/1'            | 'tests/{id}'                         | 'GET'
            'tests/1/'           | 'tests/{id}'                         | 'GET'
            'tests/1'            | 'tests/{id}'                         | 'POST'
            'tests/1/'           | 'tests/{id}'                         | 'POST'
            'tests/1/history/2'  | 'tests/{TestId}/history/{HistoryId}' | 'GET'
            'tests/1/history/2/' | 'tests/{TestId}/history/{HistoryId}' | 'GET'
    }

    @Unroll
    def "Throw #exception.simpleName if #condition"(exception, condition, path, message) {
        setup:
            def pathHandler = new ResourcePathHandler(resources)
        when:
            pathHandler.resolvePathSpecification(path, "GET")
        then:
            def thrownException = thrown(exception)
            thrownException.message == message
        where:
            exception                 | condition                                    | path                 | message
            ResourceNotFoundException | 'no specification exists for the given path' | 'tests/doesnt/exist' | "No resource could be resolved for path: tests/doesnt/exist"

    }

    @Unroll
    def "Resolve #parameters from #spec if path #path is requested"(path, spec, parameters) {
        setup:
            def resource = Mock(Resource)
            _ * resource.getPathSpec() >> spec
            def pathHandler = new ResourcePathHandler(_)
        when:
            def result = pathHandler.resolvePathParameters(path, resource)
        then:
            parameters.every { k, v -> result.getValue(k) == v }
        where:
            path                 | spec                                 | parameters
            'tests'              | 'tests'                              | []
            'tests/'             | 'tests'                              | []
            'tests/1'            | 'tests/{id}'                         | ['id': '1']
            'tests/1/'           | 'tests/{id}'                         | ['id': '1']
            'tests/1/history/2'  | 'tests/{testId}/history/{historyId}' | ['testId': '1', 'historyId': '2']
            'tests/1/history/2/' | 'tests/{testId}/history/{historyId}' | ['testId': '1', 'historyId': '2']
    }

    @Unroll
    def "Resolve parameters #parameters for path #path"(path, parameters) {
        setup:
            def pathHandler = new ResourcePathHandler(_)
        when:
            def result = pathHandler.resolvePathQueryString(path)
        then:
            parameters.every { k, v -> result.getValue(k) ==  v }
        where:
            path                                            | parameters
            'tests?version=1.0_'                            | ['version': '1.0_']
            'tests?version=1.0.*&id=100100'                 | ['version': '1.0.*', 'id': '100100']
            'tests?version=1.0-*&name=John%20Maynard+Smith' | ['version': '1.0-*', 'name': 'John Maynard Smith']
    }
}
