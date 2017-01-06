package com.temenos.responder.flows

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.ExternalCustomerInformation
import com.temenos.responder.commands.transformers.CustomerTransformer
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by aburgos on 24/12/2016.
 */
class CustomerInformationWithTransformerTest extends Specification {

    @Unroll
    def "Customer information command"(id, map, extnMap) {
        setup:
            def command = new CustomerInformationWithTransformer()
            def context = Mock(ExecutionContext)
            def externalCommand = Mock(ExternalCustomerInformation)
            def transformCommand = Mock(CustomerTransformer)
            def addLinkCommand = Mock(AddLink)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('id') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity(map))
            1 * context.setResponseCode('200')
            1 * context.getCommand(ExternalCustomerInformation) >> externalCommand
            1 * context.getCommand(CustomerTransformer) >> transformCommand
            1 * context.getCommand(AddLink) >> addLinkCommand
            1 * externalCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute("finalResult", new Entity(extnMap))
                ctx.setResponseCode('200')
            }
            1 * transformCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute('finalResult', new Entity(map))
                ctx.setResponseCode('200')
            }
        where:
            id     | map                                                                                         | extnMap
            100100 | ['CustomerId': 100100, 'CustomerName': 'John Smith', 'CustomerAddress': 'No Name Street'] | ["CUSTOMER_ID": 100100, "CUSTOMER_NAME": "John Smith", "CUSTOMER_ADDRESS": "No Name Street"]
            100200 | ['CustomerId': 100200, 'CustomerName': 'Iris Law', 'CustomerAddress': '2 Lansdowne Rd']   | ["CUSTOMER_ID": 100200, "CUSTOMER_NAME": "Iris Law", "CUSTOMER_ADDRESS": "2 Lansdowne Rd"]
    }

    @Unroll
    def "Customer information command for inexistent customers"(id) {
        setup:
            def command = new CustomerInformationWithTransformer()
            def context = Mock(ExecutionContext)
            def externalCommand = Mock(ExternalCustomerInformation)
            def addLinkCommand = Mock(AddLink)
        when:
            command.execute(context)
        then:
            _ * context.getAttribute('id') >> id
            _ * context.getAttribute('into') >> 'finalResult'
            1 * context.setAttribute('finalResult', new Entity())
            1 * context.setResponseCode('404')
            1 * context.getCommand(ExternalCustomerInformation) >> externalCommand
            1 * context.getCommand(AddLink) >> addLinkCommand
            1 * externalCommand.execute(_) >> { CommandContext ctx ->
                ctx.setAttribute('finalResult', new Entity())
                ctx.setResponseCode('404')
            }
        where:
            id << [666666, 999999]
    }
}
