package com.temenos.responder.commands.transformers

import com.temenos.responder.context.CommandContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification

/**
 * Created by aburgos on 17/01/2017.
 */
class CustomerDashboardTransformerTest extends Specification {
    def "Customer dashboard transformer converts external data format #extnData into internal data format #intnData"(){
        given:
            def command = new CustomerDashboardTransformer()
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
            1 * extnCustomer.get('CUSTOMER\\.HOME\\.ADDRESS') >> 'No Name Street'
            1 * extnCustomer.get('CUSTOMER\\.WORK\\.ADDRESS') >> '85 Albert Embankment'
            1 * commandContext.setAttribute('finalResult',
                new Entity(['customerId':100100,'customerName':'John Smith','homeAddress':'No Name Street','workAddress':'85 Albert Embankment']))
    }
}
