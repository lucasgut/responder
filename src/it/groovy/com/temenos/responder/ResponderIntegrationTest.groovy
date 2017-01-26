package com.temenos.responder

import com.temenos.responder.controller.RequestHandler
import com.temenos.responder.startup.ResponderApplication
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.glassfish.jersey.test.JerseyTest
import spock.lang.Specification
import spock.lang.Unroll

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response

/**
 * Created by Douglas Groves on 15/12/2016.
 */
class ResponderIntegrationTest extends Specification {

    @Delegate
    JerseyTest test = new JerseyTest(new ResponderApplication()) {}

    def setup() { setUp() }

    def cleanup() { tearDown() }

    def "GET request to /version returns 200 OK and returns contents of versionInfo.json"() {
        when:
            def result = target('version').request().get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['appVersion']['versionNumber'] == "0.1-SNAPSHOT"
            body['appVersion']['buildDate'] == '2016-12-09T16:00:00Z'
            body['appVersion']['blameThisPerson'] == 'Jenkins'
            body['_links']['self']['href'] == 'http://localhost:9998/version'
            body['_embedded'] != null
    }

    @Unroll
    def "POST request to /add returns 200 OK and returns the sum of #operands as #sum"(data, sum, operands) {
        given:
            def entity = Entity.json(new JsonBuilder(data).toString())
        when:
            def result = target('add').request().post(entity)
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['add']['result'] == sum
            body['add']['operands'] == null
            body['_links']['self']['href'] == 'http://localhost:9998/add'
            body['_embedded'] != null
        where:
            data                 | sum | operands
            ['operands': [1, 1]] | 2   | '1 and 1'
    }

    @Unroll
    def "GET request to /customer/#id in ExternalCustomerInformation mock command"(id, name, address) {
        setup:
            def path = 'customer/' + id
        when:
            def result = target(path).request().get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['customerInfo']['CustomerId'] == id
            body['customerInfo']['CustomerName'] == name
            body['customerInfo']['CustomerAddress'] == address
            body['_links']['self']['href'] == "http://localhost:9998/customer/${id}"
            body['_embedded'] != null
        where:
            id     | name         | address
            100100 | 'John Smith' | 'No Name Street'
            100200 | 'Iris Law'   | '2 Lansdowne Rd'
    }

    @Unroll
    def "POST request to /#resource with invalid request data returns 400 Bad Request"(data, resource) {
        when:
            def result = target(resource).request().post(Entity.json(new JsonBuilder(data).toString()))
        then:
            result.status == Response.Status.BAD_REQUEST.statusCode
        where:
            data                       | resource
            ['operands': "John Smith"] | 'add'
    }

    def "GET request to nonexistent resource returns 404 Not Found with response body 'Not Found'"() {
        when:
            def result = target('missing').request().get()
            def json = new JsonSlurper().parseText(result.readEntity(String.class));
        then:
            result.status == Response.Status.NOT_FOUND.statusCode
            json.Message == 'Not Found'
            result.headers.get('Content-Type').first() as String == 'application/json'
    }

    def "GET request to nonexistent version resource returns 404 Not Found with response body 'Not Found'"() {
        when:
            def request = target('version').request()
            request.header(RequestHandler.DEFAULT_ROUTE_ON, "nonexistent")
            def result = request.get()
            def json = new JsonSlurper().parseText(result.readEntity(String.class));
        then:
            result.status == Response.Status.NOT_FOUND.statusCode
            json.Message == 'Not Found'
            result.headers.get('Content-Type').first() as String == 'application/json'
    }

    def "GET request to /version version 2.0 returns 200 OK and returns contents of versionInfo.json"() {
        when:
            def request = target('version').request()
            request.header(RequestHandler.DEFAULT_ROUTE_ON, "2.0")
            def result = request.get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['appVersion']['versionNumber'] == "0.2-SNAPSHOT"
            body['appVersion']['buildDate'] == '2017-01-16T10:53:00Z'
            body['appVersion']['blameThisPerson'] == 'Jenkins'
            body['_links']['self']['href'] == 'http://localhost:9998/version'
            body['_embedded'] != null
    }

    @Unroll
    def "GET request to /dashboard/#id in CustomerDashboard version 1.0 mock command"(id, name, homeAddress, workAddress, relatives, accounts) {
        setup:
            def path = 'dashboard/' + id
            def request = target(path).request()
            request.header(RequestHandler.DEFAULT_ROUTE_ON, "1.0")
        when:
            def result = request.get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            def customerDashboard = body['CustomerDashboard'] as com.temenos.responder.entity.runtime.Entity
            customerDashboard.get('customerName') == name
            customerDashboard.get('homeAddress') == homeAddress
            customerDashboard.get('workAddress') == workAddress
            int relIndex = 0
            relatives.each { rel ->
                assert customerDashboard.get('relatives['+relIndex+'].name') == rel['name']
                assert customerDashboard.get('relatives['+relIndex+'].relationship') == rel['relationship']
                relIndex++
            }
            int accIdx = 0
            accounts['accounts'].each { acc ->
                def accPrefix = 'accounts[' + accIdx + ']'
                assert customerDashboard.get(accPrefix + '.accountLabel') == acc[accIdx]['accountLabel']
                assert customerDashboard.get(accPrefix + '.accountNumber') == acc[accIdx]['accountNumber']
                assert customerDashboard.get(accPrefix + '.accountBalance') == acc[accIdx]['accountBalance']
                int stoIdx = 0
                acc[accIdx]['standingOrders'].each { stoOrder ->
                    def stoPrefix = accPrefix + '.standingOrders[' + stoIdx + ']'
                    assert customerDashboard.get(stoPrefix + '.targetAccount') == stoOrder['targetAccount']
                    assert customerDashboard.get(stoPrefix + '.amount') == stoOrder['amount']
                    stoIdx++
                }
                accIdx++
            }
            body['_links']['self']['href'] == "http://localhost:9998/dashboard/${id}"
            body['_embedded'] != null
        where:
            id     | name         | homeAddress                                                      | workAddress                                                                  | relatives                                                                                           | accounts
            100100 | 'John Smith' | ['line1': 'No Name Street', 'line2': '', 'postcode': 'NW9 6LR']  | ['line1': '85 Albert Embankment', 'line2': 'Lambeth', 'postcode': 'SE1 1BD'] | [["name": "Jim Cain", "relationship": "Father"], ["name": "Rick Perry", "relationship": "Sibling"]] | ['accounts': [[['accountId': 1001, 'accountLabel': 'Savings', 'accountNumber': 'GB29 NWBK 6016 1331 9268 19', 'accountBalance': 1200000.0, 'standingOrders': []], ['accountId': 1004, 'accountLabel': 'Payments account', 'accountNumber': 'DE89 3704 0044 0532 0130 00', 'accountBalance': 500000.0, 'standingOrders': [['standingOrderId': 400, 'targetAccount': 'GB27 BOFI 9021 2729 8235 29', 'amount': 2020.0], ['standingOrderId': 401, 'targetAccount': 'GB29 NWBK 6016 1331 9268 19', 'amount': 2000.0], ['standingOrderId': 402, 'targetAccount': 'GB29 NWBK 6016 1331 9268 53', 'amount': 4000.0]]], ['accountId': 1009, 'accountLabel': 'H funding account', 'accountNumber': 'LB62 0999 0000 0001 0019 0122 9114', 'accountBalance': 9620000.0, 'standingOrders': []]]]]
            100200 | 'Iris Law'   | ['line1': '2 Lansdowne Rd', 'line2': '', 'postcode': 'CR8 2PA']  | ['line1': '9 Argyll Street', 'line2': '', 'postcode': 'SE1 9TG']             | [["name": "Jeff Barry", "relationship": "Father"], ["name": "T Mayhem", "relationship": "Mother"]]  | ['accounts': [[['accountId': 1002, 'accountLabel': 'Daily account', 'accountNumber': 'GB29 NWBK 6016 1331 9268 53', 'accountBalance': 8000.0, 'standingOrders': [['standingOrderId': 200, 'targetAccount': 'GB91 BKEN 1000 0041 6100 08', 'amount': 1200.0]]], ['accountId': 1003, 'accountLabel': 'Dubious account', 'accountNumber': 'VG96 VPVG 0000 0123 4567 8901', 'accountBalance': 68000000.0, 'standingOrders': []]]]]
    }

    @Unroll
    def "GET request to /dashboard/#id nonexistent in CustomerDashboard version 1.0 mock command"(id) {
        setup:
            def path = 'dashboard/' + id
            def request = target(path).request()
            request.header(RequestHandler.DEFAULT_ROUTE_ON, "1.0")
        when:
            def result = request.get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['CustomerDashboard'] == [:]
            body['_links']['self']['href'] == "http://localhost:9998/dashboard/${id}"
            body['_embedded'] != null
        where:
            id << [66666, 99999]
    }

    @Unroll
    def "GET request to /dashboard/#id in CustomerDashboard version 2.0 mock command"(id, name, homeAddress, workAddress, relatives, accounts) {
        setup:
            def path = 'dashboard/' + id
            def request = target(path).request()
            request.header(RequestHandler.DEFAULT_ROUTE_ON, "2.0")
        when:
            def result = request.get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            def customerDashboard = body['CustomerDashboard'] as com.temenos.responder.entity.runtime.Entity
            customerDashboard.get('customerName') == name
            customerDashboard.get('homeAddress') == homeAddress
            customerDashboard.get('workAddress') == workAddress
            int relIndex = 0
            relatives.each { rel ->
                assert customerDashboard.get('relatives['+relIndex+'].name') == rel['name']
                assert customerDashboard.get('relatives['+relIndex+'].relationship') == rel['relationship']
                relIndex++
            }
            int accIdx = 0
            accounts['accounts'].each { acc ->
                def accPrefix = 'accounts[' + accIdx + ']'
                assert customerDashboard.get(accPrefix + '.accountLabel') == acc[accIdx]['accountLabel']
                assert customerDashboard.get(accPrefix + '.accountNumber') == acc[accIdx]['accountNumber']
                assert customerDashboard.get(accPrefix + '.accountBalance') == acc[accIdx]['accountBalance']
                int stoIdx = 0
                acc[accIdx]['standingOrders'].each { stoOrder ->
                    def stoPrefix = accPrefix + '.standingOrders[' + stoIdx + ']'
                    assert customerDashboard.get(stoPrefix + '.targetAccount') == stoOrder['targetAccount']
                    assert customerDashboard.get(stoPrefix + '.amount') == stoOrder['amount']
                    stoIdx++
                }
                accIdx++
            }
            body['_links']['self']['href'] == "http://localhost:9998/dashboard/${id}"
            body['_embedded'] != null
        where:
            id     | name         | homeAddress                                                      | workAddress                                                                  | relatives                                                                                           | accounts
            100100 | 'John Smith' | ['line1': 'No Name Street', 'line2': '', 'postcode': 'NW9 6LR']  | ['line1': '85 Albert Embankment', 'line2': 'Lambeth', 'postcode': 'SE1 1BD'] | [["name": "Jim Cain", "relationship": "Father"], ["name": "Rick Perry", "relationship": "Sibling"]] | ['accounts': [[['accountId': 1001, 'accountLabel': 'Savings', 'accountNumber': 'GB29 NWBK 6016 1331 9268 19', 'accountBalance': 1200000.0, 'standingOrders': []], ['accountId': 1004, 'accountLabel': 'Payments account', 'accountNumber': 'DE89 3704 0044 0532 0130 00', 'accountBalance': 500000.0, 'standingOrders': [['standingOrderId': 400, 'targetAccount': 'GB27 BOFI 9021 2729 8235 29', 'amount': 2020.0, 'transactionDate': '2008-05-14 16:02:50'], ['standingOrderId': 401, 'targetAccount': 'GB29 NWBK 6016 1331 9268 19', 'amount': 2000.0, 'transactionDate': '2011-07-30 12:56:23'], ['standingOrderId': 402, 'targetAccount': 'GB29 NWBK 6016 1331 9268 53', 'amount': 4000.0, 'transactionDate': '1995-11-26 15:20:52']]], ['accountId': 1009, 'accountLabel': 'H funding account', 'accountNumber': 'LB62 0999 0000 0001 0019 0122 9114', 'accountBalance': 9620000.0, 'standingOrders': []]]]]
            100200 | 'Iris Law'   | ['line1': '2 Lansdowne Rd', 'line2': '', 'postcode': 'CR8 2PA']  | ['line1': '9 Argyll Street', 'line2': '', 'postcode': 'SE1 9TG']             | [["name": "Jeff Barry", "relationship": "Father"], ["name": "T Mayhem", "relationship": "Mother"]]  | ['accounts': [[['accountId': 1002, 'accountLabel': 'Daily account', 'accountNumber': 'GB29 NWBK 6016 1331 9268 53', 'accountBalance': 8000.0, 'standingOrders': [['standingOrderId': 200, 'targetAccount': 'GB91 BKEN 1000 0041 6100 08', 'amount': 1200.0, 'transactionDate': '2001-01-26 11:58:29']]], ['accountId': 1003, 'accountLabel': 'Dubious account', 'accountNumber': 'VG96 VPVG 0000 0123 4567 8901', 'accountBalance': 68000000.0, 'standingOrders': []]]]]
    }

    @Unroll
    def "GET request to /dashboard/#id nonexistent in CustomerDashboard version 2.0 mock command"(id) {
        setup:
            def path = 'dashboard/' + id
            def request = target(path).request()
            request.header(RequestHandler.DEFAULT_ROUTE_ON, "2.0")
        when:
            def result = request.get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['CustomerDashboard'] == [:]
            body['_links']['self']['href'] == "http://localhost:9998/dashboard/${id}"
            body['_embedded'] != null
        where:
            id << [66666, 99999]
    }

    @Unroll
    def "POST request to /parallelTest with request body #data returns 200 OK"(data, sum, version) {
        given:
            def entity = Entity.json(new JsonBuilder(data).toString())
        when:
            def request = target('parallelTest').request().post(entity)
            def body = new JsonSlurper().parseText(request.readEntity(String.class))
        then:
            request.status == Response.Status.OK.statusCode
            body['parallelTest']['AdditionResult'] == sum
            body['parallelTest']['VersionNumber'] == version
            body['parallelTest']['operands'] == null
            body['parallelTest']['blameThisPerson'] == null
            body['_links']['self']['href'] == 'http://localhost:9998/parallelTest'
            body['_embedded'] != null
        where:
            data                 | sum | version
            ["operands": [1, 1]] | 2   | '0.1-SNAPSHOT'
    }

    @Unroll
    def "GET request to /complexCustomer/customer/#customerId/address/#addressId returns 200 OK"(customerId, addressId, customerName, addresses) {
        when:
            def result = target("complexCustomer/customer/${customerId}/address/${addressId}").request().get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['complexCustomer']['CustomerName'] == customerName
            body['complexCustomer']['Addresses'] == addresses
        where:
            customerId | addressId | customerName | addresses
            100100     | 1         | "John Smith" | [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]
            100200     | 1         | "Iris Law"   | [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]
            100100     | 2         | "John Smith" | [["HouseNumber": 30, "Road": "Snake Pass"]]
            100200     | 2         | "Iris Law"   | [["HouseNumber": 30, "Road": "Snake Pass"]]
    }

    @Unroll
    def "GET request to /address/#addressId returns 200 OK"(addressId, data) {
        when:
            def result = target("address/${addressId}").request().get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['address']['AddressId'] == addressId
            body['address'] == data
        where:
            addressId | data
            1         | ["AddressId": 1, "Addresses": [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]]
            2         | ["AddressId": 2, "Addresses": [["HouseNumber": 30, "Road": "Snake Pass"]]]
    }

    @Unroll
    def "GET request to /customerAddressEmbed/customer/#customerId/address/#addressId returns 200 OK"(customerId, addressId, addressData, customerData) {
        when:
            def result = target("customerAddressEmbed/customer/${customerId}/address/${addressId}").request().get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['_embedded']['CustomerAddresses'] == addressData
            body['customerAddressEmbed'] == customerData
        where:
            customerId | addressId | addressData                                                                                                               | customerData
            100100     | 1         | ["AddressId": 1, "Addresses": [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]] | ['CustomerId': 100100, 'CustomerName': 'John Smith', 'CustomerAddress': 'No Name Street']
            100200     | 1         | ["AddressId": 1, "Addresses": [["HouseNumber": 1, "Road": "Station Road"], ["HouseNumber": 321, "Road": "Dustbin Road"]]] | ['CustomerId': 100200, 'CustomerName': 'Iris Law', 'CustomerAddress': '2 Lansdowne Rd']
            100100     | 2         | ["AddressId": 2, "Addresses": [["HouseNumber": 30, "Road": "Snake Pass"]]]                                                | ['CustomerId': 100100, 'CustomerName': 'John Smith', 'CustomerAddress': 'No Name Street']
            100200     | 2         | ["AddressId": 2, "Addresses": [["HouseNumber": 30, "Road": "Snake Pass"]]]                                                | ['CustomerId': 100200, 'CustomerName': 'Iris Law', 'CustomerAddress': '2 Lansdowne Rd']

    }
}
