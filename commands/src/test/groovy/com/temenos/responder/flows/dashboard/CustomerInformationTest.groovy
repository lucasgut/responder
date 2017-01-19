package com.temenos.responder.flows.dashboard

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.dashboard.T24CustomerInformation
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 24/12/2016.
 */
class CustomerInformationTest extends Specification {

    @Unroll
    def "Customer information command"(id, map) {
        setup:
            def flow = new CustomerInformation()
            def context = Mock(ExecutionContext)
            def externalCommand = Mock(T24CustomerInformation)
            def addLinkCommand = Mock(AddLink)
        when:
            flow.execute(context)
        then:
            _ * context.getAttribute('customerId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(map))
            1 * context.setResponseCode('200')
            1 * context.getCommand(T24CustomerInformation) >> externalCommand
            1 * context.getCommand(AddLink) >> addLinkCommand
            1 * externalCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute("finalResult", new Entity(map))
                ctx.setResponseCode('200')
            }
        where:
            id     | map
            100100 | ["CUSTOMER.ID": 100100, "CUSTOMER.NAME": "John Smith", "CUSTOMER.HOME.ADDRESS": "No Name Street", "CUSTOMER.WORK.ADDRESS": "85 Albert Embankment", "RELATIVES": ["0": ["NAME": "Jim Cain", "RELATIONSHIP": "Father"], "1": ["NAME": "Rick Perry", "RELATIONSHIP": "Sibling"]], 'ACCOUNTS': ["1001", "1004", "1009"]]
            100200 | ["CUSTOMER.ID": 100200, "CUSTOMER.NAME": "Iris Law", "CUSTOMER.HOME.ADDRESS": "2 Lansdowne Rd", "CUSTOMER.WORK.ADDRESS": "9 Argyll Street", "RELATIVES": ["NAME": "Jeff Barry", "RELATIONSHIP": "Father"], "1": ["NAME": "T Mayhem", "RELATIONSHIP": "Mother"], 'ACCOUNTS': ["1002", "1003"]]
    }

    @Unroll
    def "Customer information command for nonexistent customers"(id) {
        setup:
            def command = new CustomerInformation()
            def context = Mock(ExecutionContext)
            def externalCommand = Mock(T24CustomerInformation)
            def addLinkCommand = Mock(AddLink)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('customerId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity())
            1 * context.setResponseCode('404')
            1 * context.getCommand(T24CustomerInformation) >> externalCommand
            1 * context.getCommand(AddLink) >> addLinkCommand
            1 * externalCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute('finalResult', new Entity())
                ctx.setResponseCode('404')
            }
        where:
            id << [666666, 999999]
    }
}
