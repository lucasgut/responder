package com.temenos.responder.producer

import com.temenos.responder.entity.runtime.Entity
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

/**
 * Created by Douglas Groves on 20/12/2016.
 */
class EntityJsonProducer implements EntityProducer{

    @Override
    Entity deserialise(String json) {
        return new Entity(new JsonSlurper().parseText(json) as Map)
    }

    @Override
    String serialise(Entity model) {
        return new JsonBuilder(model.getValues()).toString()
    }
}
