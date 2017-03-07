package com.temenos.responder.flows.dashboard;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.*;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class Iris2Helper {

    public static Request servletRequestToIris2(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        return null;
    }

    public static void iris2ToServletResponse(Response response, HttpServletResponse servletResponse) {

    }

    public static Request jaxrsToIris2(javax.ws.rs.core.Request request, HttpHeaders headers, UriInfo uriInfo, SecurityContext securityContext) {
        return null;
    }

    public static Request jaxrsToIris2(javax.ws.rs.core.Request request, HttpHeaders headers, UriInfo uriInfo, SecurityContext securityContext, OutputStream outStream) {
        return null;
    }

    public static javax.ws.rs.core.Response iris2ToJaxRs(Response response, StreamingOutput stream) {
        return javax.ws.rs.core.Response.ok(stream)
                .status(response.getStatusCode())
                .headers(response.getHeaders())
                .build();
    }

    public static void readPayload(Response response, OutputStream outStream) {
    }

    public static void writeResponse(Response response, OutputStream outStream) {
    }
}
