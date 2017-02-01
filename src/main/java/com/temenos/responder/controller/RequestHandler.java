package com.temenos.responder.controller;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Context;

import com.temenos.responder.configuration.*;
import com.temenos.responder.context.*;
import com.temenos.responder.dispatcher.Dispatcher;
import com.temenos.responder.dispatcher.FlowDispatcher;
import com.temenos.responder.entity.runtime.Document;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.paths.PathHandler;
import com.temenos.responder.scaffold.Scaffold;
import com.temenos.responder.startup.ApplicationContext;
import com.temenos.responder.validator.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Path("/{path: .*}")
public class RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);
    public static final String DEFAULT_ROUTE_ON = "Accept-Version";
    private static final int NO_OF_EXECUTORS = 5;

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
        String serverRoot = info.getBaseUri().toString();
        String origin = info.getBaseUri().toString() + path;
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
        RequestContext requestContext = new DefaultRequestContext(serverRoot, resource, parameters, requestBody, origin);
        Dispatcher dispatcher = new FlowDispatcher(NO_OF_EXECUTORS, requestContext);

        //execute the version's flow
        Document result = dispatcher.notify(version.getFlow());
        String responseModel = version.getResponse().getModel();

        try {
            Class<Scaffold> response = (Class<Scaffold>) Class.forName(responseModel);

            //validate the entity against the scaffold model definition
            if (!ApplicationContext.getInjector(Validator.class)
                    .isValid(result.getBody(), response)) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
            }
        } catch (ClassNotFoundException e) {

            String responseModelFilename = version.getResponse().getModel().replace('.','/') + ".json";
            responseModelFilename = responseModelFilename.replaceFirst("resources/", "");
            URL url = getClass().getClassLoader().getResource(responseModelFilename);

            URI uri = null;
            try {
                uri = url.toURI();
            } catch (URISyntaxException e1) {
                e1.printStackTrace();
            }

            //validate the entity against the schema model definition
            if (!ApplicationContext.getInjector(Validator.class)
                    .isValid(result.getBody(), uri)) {
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(result).build();
            }
        }

        //construct a response
        return Response.ok().entity(result).build();
    }
}
