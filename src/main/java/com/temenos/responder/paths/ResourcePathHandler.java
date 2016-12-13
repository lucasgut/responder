package com.temenos.responder.paths;

import com.temenos.responder.entity.configuration.Resource;
import com.temenos.responder.exception.ResourceNotFoundException;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Map;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
public class ResourcePathHandler implements PathHandler {
    private final List<Resource> resources;
    private static final String NOT_FOUND_MSG = "No resource could be resolved for path: %s";

    public ResourcePathHandler(List<Resource> resources){
        this.resources = resources;
    }

    @Override
    public Resource resolvePathSpecification(String path) throws ResourceNotFoundException {
        for(Resource r : resources){
            if(r.equals(path) || pathMatchesSpec(path, r.getPathSpec())){
                return r;
            }
        }
        throw new ResourceNotFoundException(String.format(NOT_FOUND_MSG, path));
    }

    private boolean pathMatchesSpec(String path, String spec){
        String[] pathSegments = path.split("/"), specSegments = spec.split("/");
        if(pathSegments.length != specSegments.length){
            return false;
        }
        for(int i = 0; i < pathSegments.length; i++){
            if(specSegments[i].matches("\\{.+?\\}") || pathSegments[i].equals(specSegments[i])){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }
}
