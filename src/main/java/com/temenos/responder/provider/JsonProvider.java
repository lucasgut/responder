package com.temenos.responder.provider;

import com.temenos.responder.entity.runtime.Document;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.producer.DocumentProducer;
import com.temenos.responder.producer.EntityProducer;
import com.temenos.responder.startup.ApplicationContext;

import javax.ws.rs.Consumes;
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
 * TODO: Query parameter handling?
 */
@Provider
@Consumes({MediaType.APPLICATION_JSON})
public class JsonProvider implements MessageBodyReader<Entity>, MessageBodyWriter<Document> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Entity.class;
    }

    @Override
    public Entity readFrom(Class<Entity> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                           MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        StringBuilder builder = new StringBuilder();
        byte[] buff = new byte[80];
        while (entityStream.read(buff) != -1) {
            builder.append(new String(buff));
        }
        return ApplicationContext.getInjector(EntityProducer.class).deserialise(builder.toString());
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type == Document.class;
    }

    @Override
    public void writeTo(Document document, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                        MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException, WebApplicationException {
        Writer writer = new OutputStreamWriter(entityStream);
        writer.write(ApplicationContext.getInjector(DocumentProducer.class).serialise(document));
        writer.flush();
    }

    @Override
    public long getSize(Document document, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return 0;
    }


}
