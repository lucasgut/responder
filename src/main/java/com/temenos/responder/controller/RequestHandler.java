package com.temenos.responder.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.temenos.responder.context.DefaultExecutionContext;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.configuration.Resource;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.paths.PathHandler;
import com.temenos.responder.producer.EntityProducer;
import com.temenos.responder.startup.ApplicationContext;
import com.temenos.responder.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        return null;
    }

    @DELETE
    public Response delete(Entity request) {
        return null;
    }

    /**
     * TODO: Move this to a workflow executor
     */
    private Response serviceRequest(String path, String method, Entity requestBody) {
        //locate a resource corresponding to the request path
        PathHandler handler = ApplicationContext.getInstance().getInjector(PathHandler.class);
        Resource resolvedResource = handler.resolvePathSpecification(path, method);
        Parameters parameters = handler.resolvePathParameters(path, resolvedResource);
        LOGGER.info("Found: {} /{}", resolvedResource.getHttpMethod(), resolvedResource.getPathSpec());

        //validate the incoming request payload
        if (!ApplicationContext.getInstance().getInjector(Validator.class)
                .isValid(requestBody, resolvedResource.getInputModelSpec())) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        //construct execution context
        ExecutionContext ctx = new DefaultExecutionContext(
                info.getBaseUri().toString() + path,
                ApplicationContext.getInstance().getInjector(EntityProducer.class),
                ApplicationContext.getInstance().getInjector(ScriptLoader.class),
                parameters,
                requestBody
        );

        //execute the resource's workflow
        resolvedResource.getFlowSpec().execute(ctx);

        //validate the entity against the model definition
        if (!ApplicationContext.getInstance().getInjector(Validator.class)
                .isValid((Entity) ctx.getAttribute("finalResult"), resolvedResource.getOutputModelSpec().get(ctx.getResponseCode()))) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ctx.getAttribute("finalResult")).build();
        }

        //construct a response
        return Response.ok().entity(ctx.getAttribute("finalResult")).build();
    }
}
