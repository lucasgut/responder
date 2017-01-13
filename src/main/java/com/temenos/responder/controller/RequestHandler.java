package com.temenos.responder.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.temenos.responder.configuration.*;
import com.temenos.responder.context.DefaultExecutionContext;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.entity.runtime.Document;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.paths.PathHandler;
import com.temenos.responder.paths.ResourcePathHandler;
import com.temenos.responder.scaffold.Scaffold;
import com.temenos.responder.startup.ApplicationContext;
import com.temenos.responder.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

@Path("/{path: .*}")
public class RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

    @Context
    private Configuration configuration;

    @Context
    private UriInfo info;

    @GET
    public Response get() {
        return serviceRequest(info.getPath(), "GET", null);
    }

    @POST
    public Response post(Entity request) {
        return serviceRequest(info.getPath(), "POST", request);
    }

    @PUT
    public Response put(Entity request) {
        return serviceRequest(info.getPath(), "PUT", request);
    }

    @DELETE
    public Response delete(Entity request) {
        return serviceRequest(info.getPath(), "DELETE", request);
    }

    private Response serviceRequest(String path, String method, Entity requestBody) {
        //locate a resource corresponding to the request path
        PathHandler handler = ApplicationContext.getInjector(PathHandler.class);
        Resource resolvedResource = handler.resolvePathSpecification(path, method);
        Parameters parameters = handler.resolvePathParameters(path, resolvedResource);
        LOGGER.info("Found: {} /{}", resolvedResource.getDirectives().get(0).getMethod(), resolvedResource.getPath());

        // TODO: handle methods and versions
        Method firstMethod = resolvedResource.getDirectives().get(0);
        Version firstVersion = firstMethod.getVersions().get(0);

        String requestModel = null;
        if(firstVersion.getRequest() != null) {
            requestModel = firstVersion.getRequest().getModel();
            Class<Scaffold> request = null;
            try {
                request = (Class<Scaffold>) Class.forName(requestModel);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //validate the incoming request payload
            if (!ApplicationContext.getInjector(Validator.class)
                    .isValid(requestBody, request)) {
                return Response.status(Response.Status.BAD_REQUEST).build();
            }
        }

        //construct execution context
        ExecutionContext ctx = new DefaultExecutionContext(
                info.getBaseUri().toString() + path,
                resolvedResource.getName(),
                parameters,
                requestBody
        );

        //execute the resource's workflow
        firstVersion.getFlow().execute(ctx);

        String responseModel = null;
        if(firstVersion.getResponse() != null) {
            responseModel = firstVersion.getResponse().getModel();
            Class<Scaffold> response = null;
            try {
                response = (Class<Scaffold>) Class.forName(responseModel);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //validate the entity against the model definition
            if (!ApplicationContext.getInjector(Validator.class)
                    .isValid((Entity) ctx.getAttribute("finalResult"), response)) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ctx.getAttribute("finalResult")).build();
            }
        }

        //construct a response
        return Response.ok().entity(new Document((Entity)ctx.getAttribute("document.links.self"),
                new Entity(),
                (Entity)ctx.getAttribute("finalResult"),
                resolvedResource.getName())).build();
    }
}
