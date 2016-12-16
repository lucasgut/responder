package com.temenos.responder

import com.temenos.responder.controller.RequestHandler
import com.temenos.responder.paths.PathHandler
import com.temenos.responder.paths.ResourcePathHandler
import com.temenos.responder.startup.ResponderApplication
import groovy.json.JsonSlurper
import org.glassfish.jersey.server.ResourceConfig
import org.glassfish.jersey.test.JerseyTest
import spock.lang.Specification

import javax.ws.rs.core.Application

/**
 * Created by Douglas Groves on 15/12/2016.
 */
class ResponderIntegrationTest extends Specification {

    @Delegate static JerseyTest test = new JerseyTest(new ResponderApplication()){}

    def setup(){ setUp() }
    def cleanup(){ tearDown() }

    def "GET request returns 200 OK with response body 'Hello world'"(){
        when:
            def result = target('version').request().get()
            def json = new JsonSlurper().parseText(result.readEntity(String.class));
        then:
            result.status == 200
            json.Greeting == 'Hello world!'
            result.headers.get('Content-Type').first() as String == 'text/plain'
    }

    def "GET request to nonexistent resource returns 404 Not Found with response body 'Not Found'"(){
        when:
            def result = target('missing').request().get()
            def json = new JsonSlurper().parseText(result.readEntity(String.class));
        then:
            result.status == 404
            json.Message == 'Not Found'
            result.headers.get('Content-Type').first() as String == 'application/json'
    }
}
