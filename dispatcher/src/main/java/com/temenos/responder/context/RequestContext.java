package com.temenos.responder.context;

import com.temenos.responder.entity.runtime.Entity;

/**
 * Created by dgroves on 18/01/2017.
 */
public interface RequestContext extends ParameterisedContext {
    String getResourcePath();
    String getServerRoot();
    String getResourceName();
    Entity getRequestBody();
    String getOrigin();
}
