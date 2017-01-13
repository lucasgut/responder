package com.temenos.responder

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
        when:
            def entity = Entity.json(new JsonBuilder(data).toString())
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
}
