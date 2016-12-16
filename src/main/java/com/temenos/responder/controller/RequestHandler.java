package com.temenos.responder.controller;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.google.inject.Injector;
import com.temenos.responder.entity.configuration.Resource;
import com.temenos.responder.paths.PathHandler;
import com.temenos.responder.paths.ResourcePathHandler;
import com.temenos.responder.startup.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Path("/{path: .*}")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
public class RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
    private int counter = 0;

    //inject application properties and request info
    private @Context Configuration configuration;
    private @Context UriInfo info;

    @GET
    public Response get() {
        return serviceRequest(info.getPath());
    }

    @POST
    public Response post(Object request) {
        return null;
    }

    @PUT
    public Response put(Object request) {
        return null;
    }

    @DELETE
    public Response delete(Object request) {
        return null;
    }

    private Response serviceRequest(String path){
        //locate a resource corresponding to the request path
        Resource resolvedResource = ApplicationContext.getInjector(PathHandler.class).resolvePathSpecification(path);

        //construct execution context


        //validate request


        //execute a workflow attached to the resource


        //validate response


        //construct a response
        return Response.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN)
                .entity("{\"Greeting\":\"Hello world!\"}")
                .build();
    }
}
