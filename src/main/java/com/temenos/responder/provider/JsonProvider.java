package com.temenos.responder.provider;

import com.google.inject.Inject;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.producer.EntityProducer;
import com.temenos.responder.producer.Producer;
import com.temenos.responder.startup.ApplicationContext;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Created by Douglas Groves on 20/12/2016.
 */
@Provider
@Produces({MediaType.APPLICATION_JSON})
public class JsonProvider implements MessageBodyWriter<Entity> {

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Entity.class;
    }

    @Override
    public long getSize(Entity entity, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }

    @Override
    public void writeTo(Entity entity, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        new OutputStreamWriter(entityStream).write(
                ApplicationContext.getInstance()
                        .getInjector(EntityProducer.class).serialise(entity)
        );
    }
}
