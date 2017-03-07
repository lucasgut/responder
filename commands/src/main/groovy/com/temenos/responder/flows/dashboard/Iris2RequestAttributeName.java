package com.temenos.responder.flows.dashboard;

public enum Iris2RequestAttributeName implements RequestAttributeName {

   /**
    * Http request
    */
   HTTP_REQUEST;

   @Override
   public String get() {
      return name();
   }
}
