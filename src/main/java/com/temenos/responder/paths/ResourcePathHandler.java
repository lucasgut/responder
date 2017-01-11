package com.temenos.responder.paths;

import com.google.inject.Inject;
import com.temenos.responder.configuration.Resource;
import com.temenos.responder.context.Parameters;
import com.temenos.responder.exception.ResourceNotFoundException;
import com.temenos.responder.loader.ScriptLoader;
import com.temenos.responder.mapper.ResourceMapper;
import com.temenos.responder.producer.EntityProducer;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import javax.ws.rs.core.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Douglas Groves on 09/12/2016.
 */
public class ResourcePathHandler implements PathHandler {
    private final List<Resource> resources;
    private static final String NOT_FOUND_MSG = "No resource could be resolved for path: %s";

    @Inject
    public ResourcePathHandler(ScriptLoader loader, EntityProducer producer, ResourceMapper mapper){
        this.resources = loadCoreResources(loader, producer, mapper);
    }

    public ResourcePathHandler(List<Resource> resources){
        this.resources = resources;
    }

    private List<Resource> loadCoreResources(ScriptLoader loader, EntityProducer producer, ResourceMapper mapper) {
        try {
            List<String> resources = new ArrayList<>(loader.loadAll().values());
            List<Object> deserialisedJson = new ArrayList<>();
            for(String json : resources){
                deserialisedJson.add(producer.deserialise(json));
            }
            List<?> mappedResources = (List<?>) mapper.mapAll(deserialisedJson);
            List<Resource> cleanResources = new ArrayList<>();
            for (Object o : mappedResources) {
                cleanResources.add(Resource.class.cast(o));
            }
            return cleanResources;
        } catch (IOException ioe) {
            throw new RuntimeException("Unable to load resources", ioe);
        }
    }

    @Override
    public Resource resolvePathSpecification(String path, String method) throws ResourceNotFoundException {
        for(Resource r : resources){
            if((r.getPath().equals(path) || pathMatchesSpec(path, r.getPath())) && r.getMethods().get(0).getMethod().equals(method)){
                return r;
            }
        }
        throw new ResourceNotFoundException(String.format(NOT_FOUND_MSG, path),
                Response.status(Response.Status.NOT_FOUND).entity("{\"Message\":\"Not Found\", \"code\": 404}").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build());
    }

    @Override
    public Parameters resolvePathParameters(String path, Resource resource) {
        Parameters parameters = new Parameters();
        String[] pathSegments = path.split("/"), specSegments = resource.getPath().split("/");
        for(int i = 0; i < pathSegments.length; i++) {
            if (specSegments[i].matches("\\{.+?\\}")) {
                // remove brackets
                String param = specSegments[i].substring(1, specSegments[i].length()-1);
                parameters.setValue(param, pathSegments[i]);
            }
        }

        return parameters;
    }

    @Override
    public Parameters resolvePathQueryString(String path) {
        Parameters parameters = new Parameters();
        try {
            List<NameValuePair> params = URLEncodedUtils.parse(new URI(path), "UTF-8");
            for (NameValuePair param : params) {
                parameters.setValue(param.getName(), param.getValue());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return parameters;
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
