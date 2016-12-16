package com.temenos.responder.producer

/**
 * Created by Douglas Groves on 09/12/2016.
 */
interface Producer {
    def deserialise(String json);
    String serialise(model);
}