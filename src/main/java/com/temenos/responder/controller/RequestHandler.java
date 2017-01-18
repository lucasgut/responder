package com.temenos.responder.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.temenos.responder.configuration.*;
import com.temenos.responder.context.DefaultExecutionContext;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.entity.runtime.Document;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.exception.ResourceNotFoundException;
import com.temenos.responder.paths.PathHandler;
import com.temenos.responder.scaffold.Scaffold;
import com.temenos.responder.startup.ApplicationContext;
import com.temenos.responder.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Path("/{path: .*}")
public class RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
    public static final String DEFAULT_ROUTE_ON = "Accept-Version";

    @Context
    private Configuration configuration;

    @Context
    private UriInfo info;

    @GET
    public Response get(@HeaderParam(DEFAULT_ROUTE_ON) String versionName) {
        return serviceRequest(info.getPath(), "GET", versionName, null);
    }

    @POST
    public Response post(Entity request, @HeaderParam(DEFAULT_ROUTE_ON) String versionName) {
        return serviceRequest(info.getPath(), "POST", versionName, request);
    }

    @PUT
    public Response put(Entity request, @HeaderParam(DEFAULT_ROUTE_ON) String versionName) {
        return serviceRequest(info.getPath(), "PUT", versionName, request);
    }

    @DELETE
    public Response delete(Entity request, @HeaderParam(DEFAULT_ROUTE_ON) String versionName) {
        return serviceRequest(info.getPath(), "DELETE", versionName, request);
    }

    private Response serviceRequest(String path, String methodName, String versionName, Entity requestBody) {
        //locate a resource corresponding to the request path
        PathHandler handler = ApplicationContext.getInjector(PathHandler.class);
        Resource resource = handler.resolvePathSpecification(path);
        Parameters parameters = handler.resolvePathParameters(path, resource);
        LOGGER.info("Found: {} /{}", methodName, resource.getPath());

        ResourceHandler resourceHandler = new ResourceHandler();
        Version version = resourceHandler.getVersion(resource, methodName, versionName);

        String requestModel = null;
        if(version.getRequest() != null) {
            requestModel = version.getRequest().getModel();
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
                resource.getName(),
                parameters,
                requestBody
        );

        //execute the resource's workflow
        version.getFlow().execute(ctx);

        String responseModel = null;

        if(ctx.getResponseCode().equals(Response.Status.INTERNAL_SERVER_ERROR.name())) {
            responseModel = version.getError().getModel();
        } else if (version.getResponse() != null) {
            responseModel = version.getResponse().getModel();
        }

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

        //construct a response
        return Response.ok().entity(new Document((Entity)ctx.getAttribute("document.links.self"),
                new Entity(),
                (Entity)ctx.getAttribute("finalResult"),
                resource.getName())).build();
    }
}
