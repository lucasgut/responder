package com.temenos.responder.paths;

import com.temenos.responder.entity.configuration.Resource;

public interface PathHandler {
    Resource resolvePathSpecification(String path);
}
