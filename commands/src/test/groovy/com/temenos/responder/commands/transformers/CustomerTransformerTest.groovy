package com.temenos.responder.commands.transformers

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification

/**
 * Created by Douglas Groves on 04/01/2017.
 */
class CustomerTransformerTest extends Specification {
    def "Customer transformer converts external data format #extnData into internal data format #intnData"(){
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
                    new Entity(['CustomerId':100100,'CustomerName':'John Smith','CustomerAddress':'No Name Street']))
    }
}
