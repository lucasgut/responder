package com.temenos.responder.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.temenos.responder.entity.configuration.Resource;
import com.temenos.responder.paths.PathHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Path("/{path: .*}")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
    private int counter = 0;

    //inject application properties
    @Context
    private Configuration configuration;

    @GET
    public Response get(@PathParam("path") String path) {
        return doSomething(null);
    }

    @POST
    public Response post(Object request) {
        return doSomething(request);
    }

    @PUT
    public Response put(Object request) {
        return doSomething(request);
    }

    @DELETE
    public Response delete(Object request) {
        return doSomething(request);
    }

    private Response doSomething(Object request) {
        return Response.ok().entity("Hello world!").header("Content-Type", "plain/text").build();
    }
}
