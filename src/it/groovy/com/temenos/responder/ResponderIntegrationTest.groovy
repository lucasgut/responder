package com.temenos.responder

import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.startup.ResponderApplication
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import org.glassfish.jersey.test.JerseyTest
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Unroll

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
            body['versionNumber'] == 0.1
            body['buildDate'] == '2016-12-09T16:00:00Z'
            body['blameThisPerson'] == 'Jenkins'
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

    @Unroll
    def "POST request to /add returns 200 OK and returns the sum of #operands as #sum"(data, sum, operands) {
        when:
            def result = target('add').request().post(javax.ws.rs.client.Entity.json(new JsonBuilder(data).toString()))
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['result'] == sum
            body['sum'] == null
        where:
            data                 | sum | operands
            ['operands': [1, 1]] | 2   | '1 and 1'
    }

    def "GET request to /customer/#id in T24CustomerInformation mock command"(id, name, address) {
        setup:
            def path = 'customer/' + id
        when:
            def result = target(path).request().get()
            def body = new JsonSlurper().parseText(result.readEntity(String.class))
        then:
            result.status == Response.Status.OK.statusCode
            body['CustomerId'] == id
            body['CustomerName'] == name
            body['CustomerAddress'] == address
        where:
            id       | name          | address
            '100100' | 'John Smith'  | 'No Name Street'
            '100200' | 'Iris Law'    | '2 Lansdowne Rd'
    }
}
