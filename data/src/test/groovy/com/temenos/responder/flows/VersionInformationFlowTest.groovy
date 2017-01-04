package com.temenos.responder.flows

import com.temenos.responder.commands.VersionInformation
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.loader.ScriptLoader
import spock.lang.Specification

/**
 * Created by Douglas Groves on 04/01/2017.
 */
class VersionInformationFlowTest extends Specification {
    def "Version flow invokes VersionInformation and returns 200 OK"(){
        given:
            def versionInformationFlow = new VersionInformationFlow()
            def executionContext = Mock(ExecutionContext)
            def scriptLoader = Mock(ScriptLoader)
            def versionInformation = Mock(VersionInformation)
        when:
            def result = versionInformationFlow.execute(executionContext)
        then:
            0 * executionContext.getRequestBody()
            1 * executionContext.getCommand(VersionInformation) >> versionInformation
            1 * versionInformation.execute(_) >> { CommandContext ctx ->
                ctx.setResponseCode('200')
            }
            1 * executionContext.setAttribute("finalResult", _)
            1 * executionContext.setResponseCode("200")
    }
}
