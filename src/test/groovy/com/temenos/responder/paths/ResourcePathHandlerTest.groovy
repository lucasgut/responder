package com.temenos.responder.paths

import com.temenos.responder.entity.configuration.Resource
import com.temenos.responder.exception.ResourceNotFoundException;
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
            mockItemWithItemResource = Mock(Resource)
        _ * mockCollectionResource.getPathSpec() >> 'tests'
        _ * mockItemResource.getPathSpec() >> 'tests/{id}'
        _ * mockItemWithItemResource.getPathSpec() >> 'tests/{TestId}/history/{HistoryId}'
        resources = [mockCollectionResource, mockItemResource, mockItemWithItemResource]
    }

    @Unroll
    def "Resolve #spec if path #path is requested"(path, spec) {
        setup:
            def pathHandler = new ResourcePathHandler(resources)
        when:
            def result = pathHandler.resolvePathSpecification(path)
        then:
            result.pathSpec == spec
        where:
            path                 | spec
            'tests'              | 'tests'
            'tests/'             | 'tests'
            'tests/1'            | 'tests/{id}'
            'tests/1/'           | 'tests/{id}'
            'tests/1/history/2'  | 'tests/{TestId}/history/{HistoryId}'
            'tests/1/history/2/' | 'tests/{TestId}/history/{HistoryId}'
    }

    @Unroll
    def "Throw #exception.simpleName if #condition"(exception, condition, path, message) {
        setup:
            def pathHandler = new ResourcePathHandler(resources)
        when:
            pathHandler.resolvePathSpecification(path)
        then:
            def thrownException = thrown(exception)
            thrownException.message == message
        where:
            exception                 | condition                                    | path                 | message
            ResourceNotFoundException | 'no specification exists for the given path' | 'tests/doesnt/exist' | "No resource could be resolved for path: tests/doesnt/exist"

    }
}
