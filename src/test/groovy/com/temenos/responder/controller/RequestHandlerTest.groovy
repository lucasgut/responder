package com.temenos.responder.controller

import com.temenos.responder.entity.configuration.Flow
import com.temenos.responder.entity.configuration.Resource
import com.temenos.responder.paths.PathHandler
import spock.lang.Specification

/**
 * Created by Douglas Groves on 13/12/2016.
 */
class RequestHandlerTest extends Specification{
    def "GET request to #path resolves to path specification #pathSpec and routed to #flow"(path, pathSpec, flow){
        setup:
            def pathHandler = Mock(PathHandler)
            def resource = Mock(Resource)
            def myFlow = Mock(Flow)
            def requestHandler = new RequestHandler()
        when:
            def result = requestHandler.get(path)
        then:
            1 * pathHandler.resolvePathSpecification(path) >> resource
            1 * resource.getFlowSpec() >> myFlow
    }
}
