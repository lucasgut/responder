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
            100100 | ['ID': 100100, 'NAME': 'John Smith', 'HOME.ADDRESS': ["LINE1": "No Name Street", "LINE2": "", "POSTCODE": "NW9 6LR"], 'WORK.ADDRESS': ["LINE1": "85 Albert Embankment", "LINE2": "Lambeth", "POSTCODE": "SE1 1BD"], 'RELATIVES': [["NAME": "Jim Cain", "RELATIONSHIP": "Father"], ["NAME": "Rick Perry", "RELATIONSHIP": "Sibling"]], 'ACCOUNTS': ["1001", "1004", "1009"]]
            100200 | ['ID': 100200, 'NAME': 'Iris Law', 'HOME.ADDRESS': ["LINE1": "2 Lansdowne Rd", "LINE2": "", "POSTCODE": "CR8 2PA"], 'WORK.ADDRESS': ["LINE1": "9 Argyll Street", "LINE2": "", "POSTCODE": "SE1 9TG"], 'RELATIVES': [["NAME": "Jeff Barry", "RELATIONSHIP": "Father"], ["NAME": "T Mayhem", "RELATIONSHIP": "Mother"]], 'ACCOUNTS': ["1002", "1003"]]
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
