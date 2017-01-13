package com.temenos.responder.configuration;

import com.temenos.responder.flows.Flow;

/**
 * Created by aburgos on 11/01/2017.
 */
public class Version {
    private final String name;
    private final Flow flow;
    private String description;
    private Action request;
    private Action response;
    private Action error;
    private Status status;
    private String lifeCycle;

    public Version(String name, Flow flow) {
        this.name = name;
        this.flow = flow;
    }

    public String getName() {
        return name;
    }

    public Flow getFlow() {
        return flow;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setRequest(Action request) {
        this.request = request;
    }

    public Action getRequest() {
        return request;
    }

    public void setResponse(Action response) {
        this.response = response;
    }

    public Action getResponse() {
        return response;
    }

    public void setError(Action error) {
        this.error = error;
    }

    public Action getError() {
        return error;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
    }

    public void setLifeCycle(String lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    public String getLifeCycle() {
        return lifeCycle;
    }
}