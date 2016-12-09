package com.temenos.responder.producer

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class JsonProducer {
    static def deserialise(String json) {
        return new JsonSlurper().parseText(json)
    }

    static String serialise(model) {
        return new JsonBuilder(model).toString()
    }
}