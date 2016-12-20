package com.temenos.responder.controller;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.temenos.responder.context.DefaultExecutionContext;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.configuration.Resource;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.paths.PathHandler;
import com.temenos.responder.producer.Producer;
import com.temenos.responder.startup.ApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/{path: .*}")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN})
public class RequestHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestHandler.class);

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
        Resource resolvedResource = ApplicationContext.getInstance().getInjector(PathHandler.class).resolvePathSpecification(path);

        //construct execution context
        ExecutionContext ctx = new DefaultExecutionContext(
                info.getBaseUri().toString()+path,
                ApplicationContext.getInstance().getInjector(Producer.class),
                ApplicationContext.getInstance().getInjector(ScriptLoader.class)
        );

        //execute the resource's workflow
        resolvedResource.getFlowSpec().execute(ctx);

        //TODO: Add self link?



        //construct a response
        return Response.ok()
                .entity(ApplicationContext.getInstance()
                        .getInjector(Producer.class)
                        .serialise(ctx.getAttribute("finalResult")))
                .build();
    }
}
