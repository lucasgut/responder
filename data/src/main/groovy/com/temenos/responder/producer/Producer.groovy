package com.temenos.responder.producer

/**
 * Created by Douglas Groves on 09/12/2016.
 */
interface Producer<T,E> {
    T deserialise(E input);
    E serialise(T input);
}