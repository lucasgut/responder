package com.temenos.responder

import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.startup.ResponderApplication
import groovy.json.JsonSlurper
import org.glassfish.jersey.test.JerseyTest
import spock.lang.Specification

import javax.ws.rs.core.Response

/**
 * Created by Douglas Groves on 15/12/2016.
 */
class ResponderIntegrationTest extends Specification {

    @Delegate
    static JerseyTest test = new JerseyTest(new ResponderApplication()) {}

    def setup() { setUp() }

    def cleanup() { tearDown() }

    def "GET request to /version returns 200 OK and returns contents of versionInfo.json"() {
        when:
            def result = new JsonSlurper().parseText(target('version').request().get(String.class))
        then:
            result['core.VersionNumberModel']['versionNumber'] == 0.1
            result['core.VersionNumberModel']['buildDate'] == '2016-12-09T16:00:00Z'
            result['core.VersionNumberModel']['blameThisPerson'] == 'Jenkins'
            result['_links'] != null
            result['_embedded'] != null
    }


    def "GET request to nonexistent resource returns 404 Not Found with response body 'Not Found'"() {
        when:
            def result = target('missing').request().get()
            def json = new JsonSlurper().parseText(result.readEntity(String.class));
        then:
            result.status == 404
            json.Message == 'Not Found'
            result.headers.get('Content-Type').first() as String == 'application/json'
    }
}
