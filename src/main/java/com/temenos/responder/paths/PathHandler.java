package com.temenos.responder.paths;

import com.temenos.responder.configuration.Method;
import com.temenos.responder.configuration.Resource;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.exception.ResourceNotFoundException;

public interface PathHandler {
    Resource resolvePathSpecification(String path) throws ResourceNotFoundException;
    Parameters resolvePathParameters(String path, Resource resource);
    Parameters resolvePathQueryString(String path);
}
