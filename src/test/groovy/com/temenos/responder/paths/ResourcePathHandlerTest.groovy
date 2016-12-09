package com.temenos.responder.paths

import com.temenos.responder.entity.configuration.Resource;
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
        1 * mockCollectionResource.getPathSpec() >> 'tests'
        1 * mockItemResource.getPathSpec() >> 'tests/{id}'
        1 * mockItemWithItemResource.getPathSpec() >> 'tests/{testId}/history/{historyId}'
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
            path                | spec
            'tests'             | 'tests'
            'tests/1'           | 'tests/{id}'
            'tests/1/history/2' | 'tests/{testId}/history/{historyId}'
    }
}
