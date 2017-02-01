package com.temenos.responder.flows

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.Command
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.entity.runtime.Document
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldAddress
import com.temenos.responder.scaffold.ScaffoldComplexCustomer
import com.temenos.responder.scaffold.ScaffoldCustomer
import com.temenos.responder.scaffold.ScaffoldExternalAddress
import com.temenos.responder.scaffold.ScaffoldExternalCustomer
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by dgroves on 19/01/2017.
 */
class ComplexCustomerInformationTest extends Specification {

    @Unroll
    def "Invoke #flowNames in parallel, map data from #producerModelNames to #consumerModelName and set 'finalResult' to #data"(flowNames, List flows, data, producerModelNames, producerModels, consumerModelName, consumerModel) {
        given:
            def executionContext = Mock(ExecutionContext)
            def flow = new ComplexCustomerInformation()
            def customerInfoDoc = Mock(Document)
            def customerAddressDoc = Mock(Document)
            def addLink = Mock(Command)
        when:
            flow.execute(executionContext)
        then:
            executionContext.getCommand(AddLink) >> addLink
            1 * executionContext.notifyDispatchers(flows) >> ["CustomerInformation": [customerInfoDoc], "CustomerAddressFlow": [customerAddressDoc]]
            1 * executionContext.setResponseCode(200)
            1 * executionContext.setAttribute("finalResult", new Entity(data))
            customerInfoDoc.getBody() >> new Entity(["CustomerId": 1, "CustomerName": "John Smith", "CustomerAddress": "Not Known"])
            1 * customerAddressDoc.getBody() >> new Entity(["AddressId": 1, "Addresses": data['Addresses']])
        where:
            flowNames                                      | flows                                      | data                                                                                                                                    | producerModelNames                      | producerModels                      | consumerModelName         | consumerModel
            ['CustomerInformation', 'CustomerAddressFlow'] | [CustomerInformation, CustomerAddressFlow] | ["CustomerName": "John Smith", "Addresses": [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]] | ['ScaffoldCustomer', 'ScaffoldAddress'] | [ScaffoldCustomer, ScaffoldAddress] | 'ScaffoldComplexCustomer' | ScaffoldComplexCustomer
    }
}
