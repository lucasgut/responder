package com.temenos.responder.commands.dashboard

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 19/01/2017.
 */
class T24StandingOrderTest extends Specification {

    @Unroll
    def "Set 'finalResult' attribute to #map if standing order with ID #id is requested"(id, map) {
        setup:
            def command = new T24StandingOrder()
            def context = Mock(CommandContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('standingOrderId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(map))
            1 * context.setResponseCode(200)
        where:
            id   | map
            200  | ['ID': 200, 'TARGET.ACCOUNT': 'GB91 BKEN 1000 0041 6100 08', 'AMOUNT': 1200.0, 'TRANSACTION.DATE': '2001-01-26 11:58:29']
            400  | ['ID': 400, 'TARGET.ACCOUNT': 'GB27 BOFI 9021 2729 8235 29', 'AMOUNT': 2020.0, 'TRANSACTION.DATE': '2008-05-14 16:02:50']
            401  | ['ID': 401, 'TARGET.ACCOUNT': 'GB29 NWBK 6016 1331 9268 19', 'AMOUNT': 2000.0, 'TRANSACTION.DATE': '2011-07-30 12:56:23']
            402  | ['ID': 402, 'TARGET.ACCOUNT': 'GB29 NWBK 6016 1331 9268 53', 'AMOUNT': 4000.0, 'TRANSACTION.DATE': '1995-11-26 15:20:52']
    }

    @Unroll
    def "Set 'finalResult' attribute to an empty entity if a nonexistent standing order ID #id is requested"(id) {
        setup:
            def command = new T24StandingOrder()
            def context = Mock(CommandContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('standingOrderId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity())
            1 * context.setResponseCode(200)
        where:
            id << [666666, 999999]
    }
}
