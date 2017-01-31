package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.DefaultCommandContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 04/01/2017.
 */
class AddLinkTest extends Specification {

    @Unroll
    def "Create link to '#resource' from #from, set context attribute 'document.links.example' to #linkData and return status code '#code'"(resource, from, code, linkData) {
        given:
            def command = new AddLink()
            def commandContext = Mock(CommandContext)
            def myEntity = new Entity(linkData)
        when:
            command.execute(commandContext)
        then:
            1 * commandContext.from() >> from
            1 * commandContext.into() >> 'document.links.example'
            1 * commandContext.setAttribute('document.links.example', myEntity)
            1 * commandContext.setResponseCode(code)
        where:
            resource         | from                                                     | code  | linkData
            'http://0.0.0.0' | ['example', 'http://0.0.0.0', 'Example', 'Example link'] | 200   | ['example': ['href': 'http://0.0.0.0', 'name': 'Example', 'description': 'Example link']]
            'http://0.0.0.0' | ['example', 'http://0.0.0.0', 'Example']                 | 200   | ['example': ['href': 'http://0.0.0.0', 'name': 'Example']]
            'http://0.0.0.0' | ['example', 'http://0.0.0.0']                            | 200   | ['example': ['href': 'http://0.0.0.0']]
    }

    @Ignore
    def "Create link to internal resource"(resource, from, code, linkData) {
        given:
            def command = new AddLink()
            def commandContext = Mock(CommandContext)
            def myEntity = new Entity(linkData)
        when:
            command.execute(commandContext)
        then:
            1 * commandContext.from() >> from
            1 * commandContext.into() >> 'document.links.appVersion'
            1 * commandContext.setAttribute('document.links.example', myEntity)
            1 * commandContext.setResponseCode(code)
        where:
            resource                 | from                                                                   | code  | linkData
            'resources://appVersion' | ['resources://appVersion', 'appVersion', 'Application version number'] | 200   | ['Example': ['href': 'http://0.0.0.0:0/responder/version', 'name': 'appVersion', 'description': 'Application version number']]
    }

}
