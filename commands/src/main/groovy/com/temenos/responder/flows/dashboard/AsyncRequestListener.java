package com.temenos.responder.flows.dashboard;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.annotation.WebListener;
import java.io.IOException;

@WebListener
public class AsyncRequestListener implements AsyncListener {

   @Override
   public void onComplete(AsyncEvent event) throws IOException {
   }

   @Override
   public void onTimeout(AsyncEvent event) throws IOException {
   }

   @Override
   public void onError(AsyncEvent event) throws IOException {
   }

   @Override
   public void onStartAsync(AsyncEvent event) throws IOException {
   }
}
