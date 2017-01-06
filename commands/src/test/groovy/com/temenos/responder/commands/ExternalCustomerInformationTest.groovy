package com.temenos.responder.commands

import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 24/12/2016.
 */
class ExternalCustomerInformationTest extends Specification {

    @Unroll
    def "T24 customer information command"(id, map) {
        setup:
            def command = new ExternalCustomerInformation()
            def context = Mock(CommandContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('id') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(map))
            1 * context.setResponseCode('200')
        where:
            id     | map
            100100 | ['CUSTOMER_ID': 100100, 'CUSTOMER_NAME': 'John Smith', 'CUSTOMER_ADDRESS': 'No Name Street']
            100200 | ['CUSTOMER_ID': 100200, 'CUSTOMER_NAME': 'Iris Law', 'CUSTOMER_ADDRESS': '2 Lansdowne Rd']
    }


    @Unroll
    def "T24 customer information command for inexistent customers"(id) {
        setup:
            def command = new ExternalCustomerInformation()
            def context = Mock(CommandContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('id') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity())
            1 * context.setResponseCode('404')
        where:
            id << [666666, 999999]
    }
}
