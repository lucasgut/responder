package com.temenos.responder.configuration

/**
 * Created by Douglas Groves on 20/12/2016.
 */
enum Type {
    INTEGER('integer'),
    NUMBER('number'),
    STRING('string'),
    NULL('null'),
    BOOLEAN('boolean'),
    OBJECT('object'),
    ARRAY('array');

    final String type

    Type(final String type){
        this.type = type
    }
}