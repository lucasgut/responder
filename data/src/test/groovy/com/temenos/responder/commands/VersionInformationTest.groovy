package com.temenos.responder.commands

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.loader.ScriptLoader
import com.temenos.responder.producer.JsonProducer
import com.temenos.responder.producer.Producer
import spock.lang.Specification
import spock.lang.Unroll

import javax.ws.rs.core.Response

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class VersionInformationTest extends Specification {

    @Unroll
    def "Version information command injects deserialised \
        contents of #versionFile into execution context"(versionFile, dataAsJson, data, pData) {
        given:
            def command = new VersionInformation()
            def context = Mock(ExecutionContext)
            def scriptLoader = Mock(ScriptLoader)
            def producer = Mock(Producer)
        when:
            command.execute(context)
        then:
            1 * context.getScriptLoader() >> scriptLoader
            1 * context.getProducer() >> producer
            1 * scriptLoader.load(versionFile) >> dataAsJson
            1 * producer.deserialise(dataAsJson) >> pData
            _ * context.getAttribute('from') >> [versionFile]
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(data))
            1 * context.setResponseCode('200')
        where:
            versionFile        | dataAsJson                                                                              | data                                                                                      | pData
            'versionInfo.json' | '{"versionNumber":0.1,"buildDate":"2016-12-09T16:00:00Z","blameThisPerson": "Jenkins"}' | ['versionNumber': 0.1, "buildDate": '2016-12-09T16:00:00Z', 'blameThisPerson': 'Jenkins'] | ['versionNumber': 0.1, "buildDate": '2016-12-09T16:00:00Z', 'blameThisPerson': 'Jenkins']
    }

    @Unroll
    def "Version information command adds #exception.simpleName \
        to execution context if #condition"(exception, condition, fromField, intoField, expectedLoaderInvocations) {
        given:
            def command = new VersionInformation()
            def context = Mock(ExecutionContext)
            def scriptLoader = Mock(ScriptLoader)
            def producer = Mock(Producer)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('from') >> fromField
            _ * context.getAttribute('into') >> intoField
            _ * context.getScriptLoader() >> scriptLoader
            expectedLoaderInvocations * scriptLoader.load(_) >> { throw new IOException() }
            0 * context.getProducer() >> producer
            0 * producer.deserialise(_)
            1 * context.setAttribute('exception', _)
            1 * context.setResponseCode('500')
        where:
            exception                | condition                                  | fromField        | intoField     | expectedLoaderInvocations
            ScriptExecutionException | 'version information file cannot be found' | ['missing.json'] | 'finalResult' | 1
    }
}
