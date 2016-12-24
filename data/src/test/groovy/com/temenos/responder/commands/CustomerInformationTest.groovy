package com.temenos.responder.commands

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.loader.ScriptLoader
import com.temenos.responder.producer.Producer
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 24/12/2016.
 */
class CustomerInformationTest extends Specification {

    @Unroll
    def "Customer information command"(id, map) {
        setup:
            def command = new CustomerInformation()
            def context = Mock(ExecutionContext)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('id') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(map))
            1 * context.setResponseCode('200')
        where:
            id     | map
            100100 | ['CustomerId': '100100', 'CustomerName': 'John Smith', 'CustomerAddress': 'No Name Street']
            100200 | ['CustomerId': '100200', 'CustomerName': 'Iris Law', 'CustomerAddress': '2 Lansdowne Rd']
    }

    @Unroll
    def "Customer information command for inexistent customers"(id) {
        setup:
            def command = new CustomerInformation()
            def context = Mock(ExecutionContext)
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
