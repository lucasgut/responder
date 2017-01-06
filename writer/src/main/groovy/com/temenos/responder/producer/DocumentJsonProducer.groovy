package com.temenos.responder.producer

import com.temenos.responder.entity.runtime.Document
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldVersion
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

/**
 * Created by Douglas Groves on 05/01/2017.
 */
class DocumentJsonProducer implements DocumentProducer {

    @Override
    Document deserialise(String json) {
        return null;
    }

    @Override
    String serialise(Document model) {
        return new JsonBuilder(model.getDocument().getValues()).toString()
    }

}
