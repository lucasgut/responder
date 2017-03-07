package com.temenos.responder.flows.dashboard;

import com.temenos.responder.IRIS2Exception;
import com.temenos.responder.IRIS2ValidationException;

import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static com.temenos.responder.flows.dashboard.Iris2Helper.iris2ToJaxRs;

@Provider
public class IRIS2ExceptionMapper implements ExceptionMapper<Exception> {
   private Iris2Handler iris2;

   public javax.ws.rs.core.Response toResponse(Exception e) {
      Response response = iris2.handleFailure(e);
      return iris2ToJaxRs(response, outStream -> iris2.writeEntity(response, outStream));
   }

   public javax.ws.rs.core.Response toResponse(IRIS2Exception e) {
      Response response = iris2.handleFailure(e);
      return iris2ToJaxRs(response, outStream -> iris2.writeEntity(response, outStream));
   }

   public javax.ws.rs.core.Response toResponse(IRIS2ValidationException e) {
      Response response = iris2.handleFailure(e);
      return iris2ToJaxRs(response, outStream -> iris2.writeEntity(response, outStream));
   }
}
