package com.temenos.responder.paths;

import com.temenos.responder.entity.configuration.Resource;

import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Map;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
public class ResourcePathHandler implements PathHandler {
    @Override
    public Resource resolvePathSpecification(String path) {
        return null;
    }

    private final List<Resource> resources;

    public ResourcePathHandler(List<Resource> resources){
        this.resources = resources;
    }

    public Resource resolvePathSpecification(String path, int index) {
        for(Resource r : resources){
            if(r.equals(path) || pathMatchesSpec(path, r.getPathSpec())){
                return r;
            }
        }
        return null;
    }

    private boolean pathMatchesSpec(String path, String spec){
        String[] pathSegments = path.split("/");
        String[] specSegments = path.split("/");
        if(pathSegments.length != specSegments.length){
            return false;
        }
        for(String segment : pathSegments){

        }
        return false;
    }
}
