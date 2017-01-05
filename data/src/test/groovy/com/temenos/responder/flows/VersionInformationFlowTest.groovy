package com.temenos.responder.flows

import com.temenos.responder.commands.AddLink
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
            def addLinkCommand = Mock(AddLink)
            def expectedData = ['appVersion':['self':['href':'http://0.0.0.0/version']]]
        when:
            def result = versionInformationFlow.execute(executionContext)
        then:
            0 * executionContext.getRequestBody()
            1 * executionContext.getCommand(VersionInformation) >> versionInformation
            1 * executionContext.getCommand(AddLink) >> addLinkCommand
            1 * versionInformation.execute(_) >> { CommandContext ctx ->
                ctx.setResponseCode('200')
            }
            1 * addLinkCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute('document.links.self', new Entity(expectedData))
            }
            1 * executionContext.setAttribute("finalResult", _)
            1 * executionContext.setResponseCode("200")
            1 * executionContext.getSelf() >> 'http://0.0.0.0/version'
            1 * executionContext.getResourceName() >> 'appVersion'
            1 * executionContext.setAttribute("document.links.self", new Entity(expectedData))
    }
}
