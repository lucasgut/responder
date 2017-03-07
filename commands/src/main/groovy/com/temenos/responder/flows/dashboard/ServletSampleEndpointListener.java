package com.temenos.responder.flows.dashboard;


import com.temenos.responder.IRIS2Exception;
import com.temenos.responder.IRIS2ValidationException;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

import static com.temenos.responder.flows.dashboard.Iris2Helper.servletRequestToIris2;

public class ServletSampleEndpointListener {

    private ExecutorService executorService;
    private Iris2Handler iris2;

    @GET
    public void get(HttpServletRequest httpRequest,
                    HttpServletResponse httpResponse) {
        Request iris2Request = servletRequestToIris2(httpRequest, httpResponse);
        handleRequest(iris2Request, httpResponse);
    }

    @GET
    public void getAsync(HttpServletRequest httpRequest,
                         HttpServletResponse httpResponse) {
        final AsyncContext asyncContext = httpRequest.isAsyncStarted() ? httpRequest.getAsyncContext() : httpRequest.startAsync();
        Request iris2Request = servletRequestToIris2(httpRequest, httpResponse);
        executorService.submit(() -> {
            handleRequest(iris2Request, httpResponse);
            asyncContext.complete();
        });
    }

    private void handleRequest(Request request, HttpServletResponse httpResponse) {
        try {
            Response response = iris2.handleRequest(request);
            writeResponse(response, httpResponse);
        } catch (IRIS2ValidationException e) {
            Response errorResponse = iris2.handleFailure(e);
            writeResponse(errorResponse, httpResponse);
        } catch (IRIS2Exception e) {
            Response errorResponse = iris2.handleFailure(e);
            writeResponse(errorResponse, httpResponse);
        } catch (Exception e) {
            Response errorResponse = iris2.handleFailure(e);
            writeResponse(errorResponse, httpResponse);
        }
    }

    private void writeResponse(Response iris2Response, HttpServletResponse httpResponse) {
        try {
            iris2Response.getHeaders().entrySet()
                    .forEach(e -> e.getValue()
                            .forEach(value -> httpResponse.addHeader(e.getKey(), value)));
            //...
            Iris2Helper.writeResponse(iris2Response, httpResponse.getOutputStream());
        } catch (IOException e) {
            //handleRequest error
        }
    }
}
