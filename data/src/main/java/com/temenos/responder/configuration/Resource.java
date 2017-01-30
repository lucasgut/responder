package com.temenos.responder.configuration;

import java.util.List;

/**
 * Created by aburgos on 11/01/2017.
 */
public class Resource {
    private final String name;
    private String friendlyName;
    private String description;
    private final String path;
    private List<Parameter> parameters;
    private List<String> tags;
    private final List<Method> directives;

    public Resource() {
        this.name = null;
        this.path = null;
        this.directives = null;
    }

    public Resource(String name, String path, List<Method> directives) {
        this.name = name;
        this.path = path;
        this.directives = directives;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public List<Method> getDirectives() {
        return directives;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }
}
