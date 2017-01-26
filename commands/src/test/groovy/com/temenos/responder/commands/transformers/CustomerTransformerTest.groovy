package com.temenos.responder.commands.transformers

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 04/01/2017.
 */
class CustomerTransformerTest extends Specification {

    @Unroll
    def "Transform #extnData and set 'finalResult' context attribute to #intnData"(extnData, intnData) {
        given:
            def command = new CustomerTransformer()
            def commandContext = Mock(CommandContext)
            def extnCustomer = Mock(Entity)
        when:
            command.execute(commandContext)
        then:
            1 * commandContext.from() >> ['ExtnCustomer']
            1 * commandContext.into() >> 'finalResult'
            1 * commandContext.getAttribute('ExtnCustomer') >> extnCustomer
            1 * extnCustomer.get('CUSTOMER\\.ID') >> 100100
            1 * extnCustomer.get('CUSTOMER\\.NAME') >> 'John Smith'
            1 * extnCustomer.get('CUSTOMER\\.ADDRESS') >> 'No Name Street'
            1 * commandContext.setAttribute('finalResult',
                    new Entity(intnData))
        where:
            extnData                                                                                     | intnData
            ['CUSTOMER.ID': 100100, 'CUSTOMER.NAME': 'John Smith', 'CUSTOMER.ADDRESS': 'No Name Street'] | ['CustomerId': 100100, 'CustomerName': 'John Smith', 'CustomerAddress': 'No Name Street']
    }
}
