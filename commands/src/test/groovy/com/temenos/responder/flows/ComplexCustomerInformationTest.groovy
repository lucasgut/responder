package com.temenos.responder.flows

import com.temenos.responder.commands.AddLink
import com.temenos.responder.commands.Command
import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.context.Parameters
import com.temenos.responder.context.builder.ExecutionBuilder
import com.temenos.responder.context.builder.ExecutionParameterBuilder
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
        given: "Parameters AddressId and CustomerId are both set to 1"
            def executionBuilder = Mock(ExecutionBuilder)
            def executionParameterBuilder = Mock(ExecutionParameterBuilder){
                1 * into('custAddress') >> it
                1 * parameter('AddressId','1') >> it
                execution() >> executionBuilder
            }
            def otherExecutionBuilder = Mock(ExecutionBuilder){
                inParallelWith(CustomerAddressFlow) >> executionParameterBuilder
            }
            def otherExecutionParameterBuilder = Mock(ExecutionParameterBuilder){
                1 * flow(CustomerInformation) >> it
                1 * into('custInfo') >> it
                1 * parameter('id','1') >> it
                execution() >> otherExecutionBuilder
            }
            def executionContext = Mock(ExecutionContext){
                buildExecution() >> otherExecutionParameterBuilder
                getCommand(AddLink) >> Mock(Command)
                getAttribute('AddressId') >> '1'
                getAttribute('CustomerId') >> '1'
            }
        and: "Customer information flow returns an Entity with customer ID set to '1', CustomerName set to 'John Smith' and CustomerAddress set to 'Not Known'"
            def customerInfoDoc = Mock(Document){
                getBody() >> new Entity(["CustomerId": 1, "CustomerName": "John Smith", "CustomerAddress": "Not Known"])
            }
        and: "Customer address flow returns an Entity with address ID set to '1' and Addresses set to $data"
            def customerAddressDoc = Mock(Document){
                getBody() >> new Entity(["AddressId": 1, "Addresses": data['Addresses']])
            }
        and: "The entity returned by the customer information flow will be mapped to context attribute 'custInfo'"
            executionContext.getAttribute('custInfo') >> customerInfoDoc
        and: "The entity returned by the customer address flow will be mapped to context attribute 'custAddress'"
            executionContext.getAttribute('custAddress') >> customerAddressDoc
        when: "Flow ComplexCustomerInformation is executed"
            new ComplexCustomerInformation().execute(executionContext)
        then:"Flows $flows*.simpleName should be executed in parallel with parameters id=1 and AddressId=1"
            1 * executionBuilder.execute()
        and: "The response code should be 200"
            1 * executionContext.setResponseCode(200)
        and: "Data $data should mapped to context attribute name 'finalResult'"
            1 * executionContext.setAttribute("finalResult", new Entity(data))
        where:
            flowNames                                      | flows                                      | data                                                                                                                                    | producerModelNames                      | producerModels                      | consumerModelName         | consumerModel
            ['CustomerInformation', 'CustomerAddressFlow'] | [CustomerInformation, CustomerAddressFlow] | ["CustomerName": "John Smith", "Addresses": [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]] | ['ScaffoldCustomer', 'ScaffoldAddress'] | [ScaffoldCustomer, ScaffoldAddress] | 'ScaffoldComplexCustomer' | ScaffoldComplexCustomer
    }
}
