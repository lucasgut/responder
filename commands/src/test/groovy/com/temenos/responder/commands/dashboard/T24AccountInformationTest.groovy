package com.temenos.responder.commands.dashboard

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 19/01/2017.
 */
class T24AccountInformationTest extends Specification {
    @Unroll
    def "Set 'finalResult' attribute to #map if account ID '#id' is requested"(id, map) {
        setup:
            def command = new T24AccountInformation()
            def context = Mock(CommandContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('accountId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(map))
            1 * context.setResponseCode('200')
        where:
            id   | map
            1001 | ['ID': 1001, 'LABEL': 'Savings', 'NUMBER': 'GB29 NWBK 6016 1331 9268 19', 'BALANCE': 1200000.0, 'STANDING.ORDERS': []]
            1002 | ['ID': 1002, 'LABEL': 'Daily account', 'NUMBER': 'GB29 NWBK 6016 1331 9268 53', 'BALANCE': 8000.0, 'STANDING.ORDERS': ["200"]]
            1003 | ['ID': 1003, 'LABEL': 'Dubious account', 'NUMBER': 'VG96 VPVG 0000 0123 4567 8901', 'BALANCE': 68000000.0, 'STANDING.ORDERS': []]
            1004 | ['ID': 1004, 'LABEL': 'Payments account', 'NUMBER': 'DE89 3704 0044 0532 0130 00', 'BALANCE': 500000.0, 'STANDING.ORDERS': ["400", "401", "402"]]
            1009 | ['ID': 1009, 'LABEL': 'H funding account', 'NUMBER': 'LB62 0999 0000 0001 0019 0122 9114', 'BALANCE': 9620000.0, 'STANDING.ORDERS': []]
    }

    @Unroll
    def "Set 'finalResult' attribute to an empty entity if nonexistent account ID '#id' is requested"(id) {
        setup:
            def command = new T24AccountInformation()
            def context = Mock(CommandContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('accountId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity())
            1 * context.setResponseCode('200')
        where:
            id << [666666, 999999]
    }
}
