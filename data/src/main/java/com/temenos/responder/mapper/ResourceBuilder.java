package com.temenos.responder.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.temenos.responder.configuration.*;
import com.temenos.responder.flows.Flow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Created by aburgos on 10/01/2017.
 */
public class ResourceBuilder {

    public List<Resource> getResources(JsonNode mainNode) {
        List<Resource> resources = new ArrayList<>();
        ArrayNode resourcesNode = (ArrayNode) mainNode.get(ResourceSpec.RESOURCES);
        for(JsonNode resourceNode : resourcesNode) {
            Resource resource = getResource(resourceNode);
            resources.add(resource);
        }
        return resources;
    }

    public Resource getResource(JsonNode resourceNode) {
        String name = resourceNode.fieldNames().next();
        JsonNode childNode = resourceNode.get(name);
        String path = childNode.get(ResourceSpec.PATH).asText();
        List<Method> methods = getMethods(childNode);
        Resource resource = new Resource(name, path, methods);
        if(childNode.has(ResourceSpec.FRIENDLY_NAME))
            resource.setFriendlyName(childNode.get(ResourceSpec.FRIENDLY_NAME).asText());
        if(childNode.has(ResourceSpec.RESOURCE_DESCRIPTION))
            resource.setDescription(childNode.get(ResourceSpec.RESOURCE_DESCRIPTION).asText());
        if(childNode.has(ResourceSpec.TAGS)) {
            resource.setTags(getTags(childNode));
        }
        if(childNode.has(ResourceSpec.PARAMETERS)) {
            resource.setParameters(getParameters(childNode));
        }
        return resource;
    }

    private List<String> getTags(JsonNode resourceNode) {
        List<String> tags = new ArrayList<>();
        for (Iterator<String> it = resourceNode.fieldNames(); it.hasNext(); ) {
            String tag = it.next();
            tags.add(tag);
        }
        return tags;
    }

    private List<Parameter> getParameters(JsonNode resourceNode) {
        JsonNode parametersNode = resourceNode.get(ResourceSpec.PARAMETERS);
        if(parametersNode == null) {
            throw new ResourceParsingException();
        }
        List<Parameter> parameters = new ArrayList<>();
        for (Iterator<String> it = parametersNode.fieldNames(); it.hasNext(); ) {
            String name = it.next();
            JsonNode parameterNode = parametersNode.get(name);
            String type = parameterNode.get(ResourceSpec.PARAMETER_TYPE).asText();
            ParameterLocation in = ParameterLocation.valueOf(parameterNode.get(ResourceSpec.PARAMETER_IN).asText());
            Parameter parameter = new Parameter(name, type, in);
            if(parameterNode.has(ResourceSpec.PARAMETER_DESCRIPTION))
                parameter.setDescription(parameterNode.get(ResourceSpec.PARAMETER_DESCRIPTION).asText());
            parameters.add(parameter);
        }
        return parameters;
    }

    private List<Method> getMethods(JsonNode resourceNode) {
        JsonNode directivesNode = resourceNode.get(ResourceSpec.DIRECTIVES);
        if(directivesNode == null) {
            throw new ResourceParsingException();
        }
        List<Method> methods = new ArrayList<>();
        for (Iterator<String> it = directivesNode.fieldNames(); it.hasNext(); ) {
            String httpMethodString = it.next();
            HttpMethod httpMethod = HttpMethod.valueOf(httpMethodString);
            JsonNode methodNode = directivesNode.get(httpMethodString);
            Method method = getMethod(httpMethod, methodNode);
            methods.add(method);
        }
        return methods;
    }

    private Method getMethod(HttpMethod httpMethod, JsonNode methodNode) {
        JsonNode versionsNode = methodNode.get(ResourceSpec.ROUTE_TO);
        List<Version> versions = getVersions(versionsNode);
        Method method = new Method(httpMethod, versions);
        if(methodNode.has(ResourceSpec.CACHE_SECONDS))
            method.setCacheSeconds(methodNode.get(ResourceSpec.CACHE_SECONDS).asInt());
        if(methodNode.has(ResourceSpec.CACHE_REASON))
            method.setCacheReason(methodNode.get(ResourceSpec.CACHE_REASON).asText());
        if(methodNode.has(ResourceSpec.ROUTE_ON))
            method.setRouteOn(methodNode.get(ResourceSpec.ROUTE_ON).asText());
        return method;
    }

    private List<Version> getVersions(JsonNode versionsNode) {
        List<Version> versions = new ArrayList<>();
        for (Iterator<String> it = versionsNode.fieldNames(); it.hasNext(); ) {
            String versionName = it.next();
            JsonNode versionNode = versionsNode.get(versionName);
            Version version = getVersion(versionName, versionNode);
            versions.add(version);
        }
        return versions;
    }

    private Version getVersion(String versionName, JsonNode versionNode) {
        Class<Flow> flow = getFlow(versionNode);
        Version version = new Version(versionName, flow);
        if(versionNode.has(ResourceSpec.VERSION_DESCRIPTION))
            version.setDescription(versionNode.get(ResourceSpec.VERSION_DESCRIPTION).asText());
        if(versionNode.has(ResourceSpec.REQUEST)) {
            JsonNode reqActionNode = versionNode.get(ResourceSpec.REQUEST);
            Action request = getAction(reqActionNode);
            version.setRequest(request);
        }
        if(versionNode.has(ResourceSpec.RESPONSE)) {
            JsonNode resActionNode = versionNode.get(ResourceSpec.RESPONSE);
            Action response = getAction(resActionNode);
            version.setResponse(response);
        }
        if(versionNode.has(ResourceSpec.ERROR)) {
            JsonNode errActionNode = versionNode.get(ResourceSpec.ERROR);
            Action error = getAction(errActionNode);
            version.setError(error);
        }
        if(versionNode.has(ResourceSpec.STATUS)) {
            Status status = Status.valueOf(versionNode.get(ResourceSpec.STATUS).asText());
            version.setStatus(status);
        }
        if(versionNode.has(ResourceSpec.LIFE_CYCLE))
            version.setLifeCycle(versionNode.get(ResourceSpec.LIFE_CYCLE).asText());
        return version;
    }

    private Class<Flow> getFlow(JsonNode versionNode) {
        Class<Flow> flowClass = null;
        try {
            flowClass = (Class<Flow>) Class.forName(versionNode.get(ResourceSpec.FLOW).asText());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return flowClass;
    }

    private Action getAction(JsonNode actionNode) {
        String description = actionNode.get(ResourceSpec.ACTION_DESCRIPTION).asText();
        Multiplicity multiplicity = Multiplicity.valueOf(Multiplicity.ITEM.name());
        if(actionNode.has(Multiplicity.COLLECTION.getValue())) {
            multiplicity = Multiplicity.valueOf(Multiplicity.COLLECTION.name());
        }
        String model = actionNode.get(multiplicity.getValue()).asText();
        Action action = new Action(description, multiplicity, model);
        return action;
    }
}