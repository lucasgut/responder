package com.temenos.responder.controller;

import com.temenos.responder.configuration.Method;
import com.temenos.responder.configuration.Resource;
import com.temenos.responder.configuration.Version;
import com.temenos.responder.exception.MethodNotFoundException;
import com.temenos.responder.exception.ResourceNotFoundException;
import com.temenos.responder.exception.VersionNotFoundException;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by aburgos on 13/01/2017.
 */
public class ResourceHandler {

    private static final String DEFAULT_VERSION = "default";
    private static final String VERSION_NOT_FOUND_MSG = "No version %s found for resource %s";
    private static final String METHOD_NOT_FOUND_MSG = "No method %s found for resource %s";

    public Method getMethod(Resource resource, String methodName) {
        List<Method> methods = resource.getDirectives();
        for(Method method : methods) {
            if(methodName.equals(method.getMethod().getValue()))
                return method;
        }

        throw new MethodNotFoundException(String.format(METHOD_NOT_FOUND_MSG, methodName, resource.getName()),
                Response.status(Response.Status.NOT_FOUND).entity("{\"Message\":\"Not Found\", \"code\": 404}").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build());
    }

    public Version getVersion(Resource resource, String methodName, String versionName) throws ResourceNotFoundException {
        Method method = getMethod(resource, methodName);
        List<Version> versions = method.getVersions();
        if(versionName == null)
            return doGetVersion(resource, versions, DEFAULT_VERSION);
        else
            return doGetVersion(resource, versions, versionName);
    }

    private Version doGetVersion(Resource resource, List<Version> versions, String versionName) throws ResourceNotFoundException {
        for(Version version : versions) {
            if(versionName.equals(version.getName()))
                return version;
        }

        throw new VersionNotFoundException(String.format(VERSION_NOT_FOUND_MSG, versionName, resource.getName()),
                Response.status(Response.Status.NOT_FOUND).entity("{\"Message\":\"Not Found\", \"code\": 404}").header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON).build());
    }
}
