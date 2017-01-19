package com.temenos.responder.flows.dashboard

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.dashboard.T24AccountInformation
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 19/01/2017.
 */
class AccountInformationTest extends Specification {

    @Unroll
    def "Account information flow"(id, map) {
        setup:
            def flow = new AccountInformation()
            def context = Mock(ExecutionContext)
            def externalCommand = Mock(T24AccountInformation)
            def addLinkCommand = Mock(AddLink)
        when:
            flow.execute(context)
        then:
            _ * context.getAttribute('accountId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(map))
            1 * context.setResponseCode('200')
            1 * context.getCommand(T24AccountInformation) >> externalCommand
            1 * context.getCommand(AddLink) >> addLinkCommand
            1 * externalCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute("finalResult", new Entity(map))
                ctx.setResponseCode('200')
            }
        where:
            id   | map
            1001 | ['ID': 1001, 'LABEL': 'Savings', 'NUMBER': 'GB29 NWBK 6016 1331 9268 19', 'BALANCE': 1200000, 'STANDING.ORDERS': [:]]
            1002 | ['ID': 1002, 'LABEL': 'Daily account', 'NUMBER': 'GB29 NWBK 6016 1331 9268 53', 'BALANCE': 8000, 'STANDING.ORDERS': ["200"]]
            1003 | ['ID': 1003, 'LABEL': 'Dubious account', 'NUMBER': 'VG96 VPVG 0000 0123 4567 8901', 'BALANCE': 68000000, 'STANDING.ORDERS': [:]]
            1004 | ['ID': 1004, 'LABEL': 'Payments account', 'NUMBER': 'DE89 3704 0044 0532 0130 00', 'BALANCE': 500000, 'STANDING.ORDERS': ["400", "401", "402"]]
            1009 | ['ID': 1009, 'LABEL': 'H funding account', 'NUMBER': 'LB62 0999 0000 0001 0019 0122 9114', 'BALANCE': 9620000, 'STANDING.ORDERS': [:]]
    }

    @Unroll
    def "Account information flow for nonexistent customers"(id) {
        setup:
            def flow = new AccountInformation()
            def context = Mock(ExecutionContext)
            def externalCommand = Mock(T24AccountInformation)
            def addLinkCommand = Mock(AddLink)
        when:
            flow.execute(context)
        then:
            _ * context.getAttribute('accountId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity())
            1 * context.setResponseCode('200')
            1 * context.getCommand(T24AccountInformation) >> externalCommand
            1 * context.getCommand(AddLink) >> addLinkCommand
            1 * externalCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute('finalResult', new Entity())
                ctx.setResponseCode('200')
            }
        where:
            id << [666666, 999999]
    }
}
