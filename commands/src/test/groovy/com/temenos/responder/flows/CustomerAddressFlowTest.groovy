package com.temenos.responder.flows

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.Command
import com.temenos.responder.commands.ExternalCustomerAddress
import com.temenos.responder.context.CommandContext
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.context.Parameters
import com.temenos.responder.entity.runtime.Entity
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by dgroves on 19/01/2017.
 */
class CustomerAddressFlowTest extends Specification {

    @Unroll
    def "Return #responseCode and add #data to execution context"(responseCode, data) {
        given:
            def flow = new CustomerAddressFlow()
            def executionContext = Mock(ExecutionContext)
            def customerAddress = Mock(ExternalCustomerAddress)
        when:
            flow.execute(executionContext)
        then:
            1 * executionContext.getCommand(ExternalCustomerAddress) >> customerAddress
            1 * executionContext.getCommand(AddLink) >> Mock(Command)
            1 * customerAddress.execute(_) >> { CommandContext ctx -> ctx.setAttribute("finalResult", new Entity(["STREET":[["STREET":"Station Road"],["STREET":"Dustbin Road"]],"ADDRESS":[["ADDRESS":[["ADDRESS":"1 Station Road"],["ADDRESS":"321 Dustbin Road"]]]],"TOWN.COUNTRY":[["TOWN.COUNTRY":"Hitchin, GB"],["TOWN.COUNTRY":"Teddington, GB"]],"POST.CODE":[["POST.CODE":"AL5 2TH"],["POST.CODE":"TW11 0AZ"]],"COUNTRY":[["COUNTRY":"GB"],["COUNTRY":"GB"]],"PHONE.1":[["PHONE.1":"0"],["PHONE.1":"0"]],"SMS.1":[["SMS.1":"1"]],"EMAIL.1":[["EMAIL.1":"foo@bar.com"]],"OFF.PHONE":[["OFF.PHONE":"2"]],"FAX.1":[["FAX.1":"3"]],"SECURE.MESSAGE":[["SECURE.MESSAGE":"Hello, World!"]]])) }
            1 * executionContext.setAttribute("finalResult", new Entity(data))
            executionContext.getAttribute("AddressId") >> 1
        where:
            responseCode | data
            "200"        | ["AddressId": 1, "Addresses": [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]]
    }
}
