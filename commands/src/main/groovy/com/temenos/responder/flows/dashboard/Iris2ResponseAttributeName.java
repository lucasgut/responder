package com.temenos.responder.flows.dashboard;

public enum Iris2ResponseAttributeName implements RequestAttributeName {

   /**
    * Http response
    */
   HTTP_RESPONSE;

   @Override
   public String get() {
      return name();
   }
}
