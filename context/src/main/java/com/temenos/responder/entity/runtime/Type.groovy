package com.temenos.responder.entity.runtime

/**
 * Created by Douglas Groves on 20/12/2016.
 */
enum Type {
    INTEGER('integer', Integer),
    NUMBER('number', Number),
    STRING('string', String),
    NULL('null', Void),
    BOOLEAN('boolean', Boolean),
    OBJECT('object', Map),
    ARRAY('array', List);

    final String type
    final Class<?> staticType

    Type(final String type, final Class<?> staticType){
        this.type = type
        this.staticType = staticType
    }

    String getType() {
        return type
    }

    Class<?> getStaticType() {
        return staticType
    }
}