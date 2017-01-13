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
        ArrayNode resourcesNode = (ArrayNode) mainNode.get("resources");
        for(JsonNode resourceNode : resourcesNode) {
            Resource resource = getResource(resourceNode);
            resources.add(resource);
        }
        return resources;
    }

    public Resource getResource(JsonNode resourceNode) {
        String name = null;
        for (Iterator<String> it = resourceNode.fieldNames(); it.hasNext(); ) {
            name = it.next();
        }
        JsonNode childNode = resourceNode.get(name);
        String path = childNode.get(Resource.PATH).asText();
        List<Method> methods = getMethods(childNode);
        Resource resource = new Resource(name, path, methods);
        if(childNode.has(Resource.FRIENDLY_NAME))
            resource.setFriendlyName(childNode.get(Resource.FRIENDLY_NAME).asText());
        if(childNode.has(Resource.DESCRIPTION))
            resource.setDescription(childNode.get(Resource.DESCRIPTION).asText());
        if(childNode.has(Resource.TAGS)) {
            resource.setTags(getTags(childNode));
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

    private List<Method> getMethods(JsonNode jsonNode) {
        JsonNode directivesNode = jsonNode.get(Resource.DIRECTIVES);
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
        JsonNode versionsNode = methodNode.get(Method.ROUTE_TO);
        List<Version> versions = getVersions(versionsNode);
        Method method = new Method(httpMethod, versions);
        if(methodNode.has(Method.CACHE_SECONDS))
            method.setCacheSeconds(methodNode.get(Method.CACHE_SECONDS).asInt());
        if(methodNode.has(Method.CACHE_REASON))
            method.setCacheReason(methodNode.get(Method.CACHE_REASON).asText());
        if(methodNode.has(Method.ROUTE_ON))
            method.setRouteOn(methodNode.get(Method.ROUTE_ON).asText());
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
        Flow flow = getFlow(versionNode);
        Version version = new Version(versionName, flow);
        if(versionNode.has(Version.DESCRIPTION))
            version.setDescription(versionNode.get(Version.DESCRIPTION).asText());
        if(versionNode.has(Version.REQUEST)) {
            JsonNode reqActionNode = versionNode.get(Version.REQUEST);
            Action request = getAction(reqActionNode);
            version.setRequest(request);
        }
        if(versionNode.has(Version.RESPONSE)) {
            JsonNode resActionNode = versionNode.get(Version.RESPONSE);
            Action response = getAction(resActionNode);
            version.setResponse(response);
        }
        if(versionNode.has(Version.ERROR)) {
            JsonNode errActionNode = versionNode.get(Version.ERROR);
            Action error = getAction(errActionNode);
            version.setError(error);
        }
        if(versionNode.has(Version.STATUS)) {
            Status status = Status.valueOf(versionNode.get(Version.STATUS).asText());
            version.setStatus(status);
        }
        if(versionNode.has(Version.LIFE_CYCLE))
            version.setLifeCycle(versionNode.get(Version.LIFE_CYCLE).asText());
        return version;
    }

    private Flow getFlow(JsonNode versionNode) {
        Class<Flow> flowClass = null;
        try {
            flowClass = (Class<Flow>) Class.forName(versionNode.get(Version.FLOW).asText());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Flow flow = null;
        try {
            flow = flowClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return flow;
    }

    private Action getAction(JsonNode actionNode) {
        String description = actionNode.get(Action.DESCRIPTION).asText();
        Multiplicity multiplicity = Multiplicity.valueOf(Multiplicity.ITEM.name());;
        if(actionNode.has(Multiplicity.COLLECTION.getValue())) {
            multiplicity = Multiplicity.valueOf(Multiplicity.COLLECTION.name());
        }
        String model = actionNode.get(multiplicity.getValue()).asText();
        Action action = new Action(description, multiplicity, model);
        return action;
    }
}