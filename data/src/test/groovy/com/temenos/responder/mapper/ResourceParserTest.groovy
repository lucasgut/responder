package com.temenos.responder.mapper

import com.google.gson.JsonObject
import com.temenos.responder.configuration.Method
import spock.lang.Specification

/**
 * Created by aburgos on 10/01/2017.
 */
class ResourceParserTest extends Specification {

    def "Build resource from JsonObject using GSON"(name, path, methods) {
        given:
            def parser = new ResourceParser()
            def jsonObject = new JsonObject()
            jsonObject.addProperty("name", name)
            jsonObject.addProperty("path", path)
            jsonObject.addProperty("directives", methods)
        when:
            def resource = parser.buildResource(jsonObject);
        then:
            resource.getPath() == path
        where:
            name       | path                | methods
            "dashbord" | '/accountDashboard' | ["GET", "POST"]
    }

    def "Build resource without mandatory fields throws exception"(path, method) {
        given:
        def parser = new ResourceParser()
        def jsonObject = new JsonObject()
        jsonObject.addProperty("path", path)
        //jsonObject.addProperty("method", method)
        when:
        parser.buildResource(jsonObject);
        then:
        thrown(ResourceParsingException)
        where:
        path       | method
        "/account" | "GET"
    }
}