package com.temenos.responder.mapper;

import com.temenos.responder.configuration.Method;
import com.temenos.responder.configuration.Resource;
import com.temenos.responder.configuration.Version;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

import java.util.List;

/**
 * Created by aburgos on 10/01/2017.
 */
public class ResourceParser {

    public Resource buildResource(JsonObject jsonObject) {
        JsonElement nameElement = jsonObject.get("name");
        if(nameElement == null) {
            throw new ResourceParsingException("Error parsing resource: name is null");
        }
        String name = nameElement.getAsString();
        JsonElement pathElement = jsonObject.get("path");
        if(pathElement == null) {
            throw new ResourceParsingException("Error parsing resource: path is null");
        }
        String path = pathElement.getAsString();
        List<Method> methods = getMethods(jsonObject);
        Resource resource = new Resource(name, path, methods);
        return resource;
    }

    private List<Method> getMethods(JsonObject jsonObject) {
        JsonElement directivesElement = jsonObject.get("directives");
        if(directivesElement == null) {
            throw new ResourceParsingException();
        }
        List<Method> methods = (List<Method>) directivesElement;
        return methods;
    }

    private List<Version> getVersions(JsonObject jsonObject) {
        List<Version> versions = (List<Version>) jsonObject.get("version");
        return versions;
    }
}