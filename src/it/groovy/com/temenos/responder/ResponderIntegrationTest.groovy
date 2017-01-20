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
    def "GET request to /dashboard/#id in CustomerDashboard version 1.0 mock command"(id, name, homeAddress, workAddress) {
        setup:
            def path = 'dashboard/' + id
            def request = target(path).request()
            request.header(RequestHandler.DEFAULT_ROUTE_ON, "1.0")
        when:
            def result = request.get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['CustomerDashboard']['customerName'] == name
            body['CustomerDashboard']['homeAddress'] == homeAddress
            body['CustomerDashboard']['workAddress'] == workAddress
            body['_links']['self']['href'] == "http://localhost:9998/dashboard/${id}"
            body['_embedded'] != null
        where:
            id     | name         | homeAddress      | workAddress
            100100 | 'John Smith' | 'No Name Street' | '85 Albert Embankment'
            100200 | 'Iris Law'   | '2 Lansdowne Rd' | '9 Argyll Street'
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
