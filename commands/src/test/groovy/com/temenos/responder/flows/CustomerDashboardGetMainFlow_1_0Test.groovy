package com.temenos.responder.flows

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.transformers.CustomerDashboardTransformer
import com.temenos.responder.commands.transformers.CustomerTransformer
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 17/01/2017.
 */
class CustomerDashboardGetMainFlow_1_0Test extends Specification {
    @Unroll
    def "Customer information command"(id, map, extnMap) {
        setup:
            def command = new CustomerDashboardGetMainFlow_1_0()
            def context = Mock(ExecutionContext)
            def externalCommand = Mock(ExternalCustomerDashboard)
            def transformCommand = Mock(CustomerDashboardTransformer)
            def addLinkCommand = Mock(AddLink)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('customerId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(map))
            1 * context.setResponseCode('200')
            1 * context.getCommand(ExternalCustomerDashboard) >> externalCommand
            1 * context.getCommand(CustomerDashboardTransformer) >> transformCommand
            1 * context.getCommand(AddLink) >> addLinkCommand
            1 * externalCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute("finalResult", new Entity(extnMap))
                ctx.setResponseCode('200')
            }
            1 * transformCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute('finalResult', new Entity(map))
                ctx.setResponseCode('200')
            }
        where:
            id     | map                                                                                                                          | extnMap
            100100 | ['customerId': 100100, 'customerName': 'John Smith', 'homeAddress': 'No Name Street', 'workAddress': '85 Albert Embankment'] | ["CUSTOMER.ID": 100100, "CUSTOMER.NAME": "John Smith", "CUSTOMER.HOME.ADDRESS": "No Name Street", "CUSTOMER.WORK.ADDRESS": "85 Albert Embankment"]
            100200 | ['customerId': 100200, 'customerName': 'Iris Law', 'homeAddress': '2 Lansdowne Rd', 'workAddress': '9 Argyll Street']        | ["CUSTOMER.ID": 100200, "CUSTOMER.NAME": "Iris Law", "CUSTOMER.HOME.ADDRESS": "2 Lansdowne Rd", "CUSTOMER.WORK.ADDRESS": "9 Argyll Street"]
    }

    @Unroll
    def "Customer information command for nonexistent customers"(id) {
        setup:
            def command = new CustomerDashboardGetMainFlow_1_0()
            def context = Mock(ExecutionContext)
            def externalCommand = Mock(ExternalCustomerDashboard)
            def addLinkCommand = Mock(AddLink)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('customerId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity())
            1 * context.setResponseCode('200')
            1 * context.getCommand(ExternalCustomerDashboard) >> externalCommand
            1 * context.getCommand(AddLink) >> addLinkCommand
            1 * externalCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute('finalResult', new Entity())
                ctx.setResponseCode('200')
            }
        where:
            id << [666666, 999999]
    }
}
