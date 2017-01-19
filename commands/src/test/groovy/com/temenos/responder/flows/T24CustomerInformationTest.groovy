package com.temenos.responder.flows

import com.temenos.responder.commands.dashboard.T24CustomerInformation
import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 24/12/2016.
 */
class T24CustomerInformationTest extends Specification {

    @Unroll
    def "T24 customer information command"(id, map) {
        setup:
            def command = new T24CustomerInformation()
            def context = Mock(CommandContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('customerId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(map))
            1 * context.setResponseCode('200')
        where:
            id     | map
            100100 | ['ID': 100100, 'NAME': 'John Smith', 'HOME.ADDRESS': 'No Name Street', 'WORK.ADDRESS': '85 Albert Embankment', 'RELATIVES': ["0": ["NAME": "Jim Cain", "RELATIONSHIP": "Father"], "1": ["NAME": "Rick Perry", "RELATIONSHIP": "Sibling"]], 'ACCOUNTS': ["1001", "1004", "1009"]]
            100200 | ['ID': 100200, 'NAME': 'Iris Law', 'HOME.ADDRESS': '2 Lansdowne Rd', 'WORK.ADDRESS': '9 Argyll Street', 'RELATIVES': ["0": ["NAME": "Jeff Barry", "RELATIONSHIP": "Father"], "1": ["NAME": "T Mayhem", "RELATIONSHIP": "Mother"]], 'ACCOUNTS': ["1002", "1003"]]
    }

    @Unroll
    def "T24 customer information command for nonexistent customers"(id) {
        setup:
            def command = new T24CustomerInformation()
            def context = Mock(CommandContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('customerId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity())
            1 * context.setResponseCode('200')
        where:
            id << [666666, 999999]
    }
}
