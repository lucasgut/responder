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

/**
 * Created by dgroves on 19/01/2017.
 */
class ComplexCustomerInformationTest extends Specification {
    /**
     * TODO: Use CustomerName field in consumer model
     * @param flows
     * @param data
     * @param producerModels
     * @param consumerModel
     * @return
     */
    def "Complex customer information flow invokes #flows in parallel and maps #data from #producerModels to #consumerModel"(List flows, data, producerModels, consumerModel) {
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
            1 * executionContext.setResponseCode("200")
            1 * executionContext.setAttribute("finalResult", new Entity(data))
            customerInfoDoc.getBody() >> new Entity(["CustomerId": 1, "CustomerName": "John Smith", "CustomerAddress": "Not Known"])
            1 * customerAddressDoc.getBody() >> new Entity(["AddressId": 1, "Addresses": data['Addresses']])
        where:
            flows                                      | data                                                                                                                                    | producerModels                      | consumerModel
            [CustomerInformation, CustomerAddressFlow] | ["CustomerName": "John Smith", "Addresses": [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]] | [ScaffoldCustomer, ScaffoldAddress] | ScaffoldComplexCustomer
    }
}
