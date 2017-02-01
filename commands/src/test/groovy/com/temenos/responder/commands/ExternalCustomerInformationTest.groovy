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
    def "Set 'finalResult' context attribute to #map and return status code '200' if a customer with ID #id is requested"(id, map) {
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
            100100 | ['CUSTOMER.ID': 100100, 'CUSTOMER.NAME': 'John Smith', 'CUSTOMER.ADDRESS': 'No Name Street']
            100200 | ['CUSTOMER.ID': 100200, 'CUSTOMER.NAME': 'Iris Law', 'CUSTOMER.ADDRESS': '2 Lansdowne Rd']
    }


    @Unroll
    def "Set 'finalResult' context attribute to a blank entity and return status code '404' if nonexistent customer ID #id is requested"(id) {
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
