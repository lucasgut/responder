package com.temenos.responder.flows

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 24/12/2016.
 */
class ExternalCustomerDashboardTest extends Specification {

    @Unroll
    def "T24 customer information command"(id, map) {
        setup:
            def command = new ExternalCustomerDashboard()
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
            100100 | ['CUSTOMER.ID': 100100, 'CUSTOMER.NAME': 'John Smith', 'CUSTOMER.HOME.ADDRESS': 'No Name Street', 'CUSTOMER.WORK.ADDRESS': '85 Albert Embankment']
            100200 | ['CUSTOMER.ID': 100200, 'CUSTOMER.NAME': 'Iris Law', 'CUSTOMER.HOME.ADDRESS': '2 Lansdowne Rd', 'CUSTOMER.WORK.ADDRESS': '9 Argyll Street']
    }

    @Unroll
    def "T24 customer information command for nonexistent customers"(id, error) {
        setup:
            def command = new ExternalCustomerDashboard()
            def context = Mock(CommandContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('customerId') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(error))
            1 * context.setResponseCode('404')
        where:
            id     | error
            666666 | ['ERROR.CODE': 404, 'ERROR.TEXT': 'No customer found', 'ERROR.INFO': 'Unknown customer']
            999999 | ['ERROR.CODE': 404, 'ERROR.TEXT': 'No customer found', 'ERROR.INFO': 'Unknown customer']
    }
}
