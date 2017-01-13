package com.temenos.responder.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.temenos.responder.configuration.*;
import com.temenos.responder.configuration.HttpMethod;
import com.temenos.responder.context.DefaultExecutionContext;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.entity.runtime.Document;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.Flow;
import com.temenos.responder.paths.PathHandler;
import com.temenos.responder.paths.ResourcePathHandler;
import com.temenos.responder.scaffold.Scaffold;
import com.temenos.responder.startup.ApplicationContext;
import com.temenos.responder.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    private Method getMethod(Resource resource, String methodName) {
        List<Method> methods = resource.getDirectives();
        for(Method method : methods) {
            if(methodName.equals(method.getMethod().getValue()))
                return method;
        }
        return null;
    }

    private Version getVersion(Resource resource, String methodName, String versionName) {
        Method method = getMethod(resource, methodName);
        List<Version> versions = method.getVersions();
        for(Version version : versions) {
            if(versionName.equals(version.getName()))
                return version;
        }
        return null;
    }

    private Response serviceRequest(String path, String method, Entity requestBody) {
        //locate a resource corresponding to the request path
        PathHandler handler = ApplicationContext.getInjector(PathHandler.class);
        Resource resource = handler.resolvePathSpecification(path);
        Parameters parameters = handler.resolvePathParameters(path, resource);
        LOGGER.info("Found: {} / {}", method, resource.getPath());

        // TODO: get version name from http headers
        Version version = getVersion(resource, method, "default");

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
        if(version.getResponse() != null) {
            responseModel = version.getResponse().getModel();
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
                resource.getName())).build();
    }
}
