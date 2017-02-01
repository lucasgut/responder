package com.temenos.responder.context;

import com.temenos.responder.entity.runtime.Entity;

/**
 * Created by dgroves on 18/01/2017.
 */
public interface RequestContext extends Context {
    String getResourcePath();
    String getServerRoot();
    String getResourceName();
    Parameters getRequestParameters();
    Entity getRequestBody();
    String getOrigin();
}
