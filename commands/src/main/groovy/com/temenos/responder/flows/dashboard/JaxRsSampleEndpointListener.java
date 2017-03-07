package com.temenos.responder.flows.dashboard;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import java.util.concurrent.ExecutorService;

import static com.temenos.responder.flows.dashboard.Iris2Helper.iris2ToJaxRs;
import static com.temenos.responder.flows.dashboard.Iris2Helper.jaxrsToIris2;
import static com.temenos.responder.flows.dashboard.Iris2Helper.writeResponse;

public class JaxRsSampleEndpointListener {

    private ExecutorService executorService;
    private Iris2Handler iris2;

    // Inefficient
    @GET
    public void getByPassJaxRs(
                        @Context HttpServletRequest httpRequest,
                        @Context HttpServletResponse httpResponse) {
        //See ServletSampleEndpointListener.get()
    }

    @GET
    public javax.ws.rs.core.Response get(javax.ws.rs.core.Request jaxRsRequest,
                                         @Context HttpHeaders headers,
                                         @Context UriInfo uriInfo,
                                         @Context SecurityContext securityContext) {
        Request request = jaxrsToIris2(jaxRsRequest, headers, uriInfo, securityContext);
        Response response = iris2.handleRequest(request);
        return iris2ToJaxRs(response, outStream -> writeResponse(response, outStream));
    }

    @GET
    public void getAsync(javax.ws.rs.core.Request jaxRsRequest,
                         @Context HttpHeaders headers,
                         @Context UriInfo uriInfo,
                         @Context SecurityContext securityContext,
                         @Suspended final AsyncResponse asyncJaxRsResponse) {
        Request request = jaxrsToIris2(jaxRsRequest, headers, uriInfo, securityContext);
        executorService.submit(() -> {
            Response response = iris2.handleRequest(request);
            javax.ws.rs.core.Response jaxRsResponse = iris2ToJaxRs(response, outStream -> writeResponse(response, outStream));
            asyncJaxRsResponse.resume(jaxRsResponse);
        });
    }
}
