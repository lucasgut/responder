package com.temenos.responder.paths;

import com.temenos.responder.configuration.Resource;
import com.temenos.responder.exception.ResourceNotFoundException;

public interface PathHandler {
    Resource resolvePathSpecification(String path) throws ResourceNotFoundException;
}
