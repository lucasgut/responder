package com.temenos.responder.configuration;

import com.temenos.responder.flows.Flow;

/**
 * Created by aburgos on 11/01/2017.
 */
public class Version {
    private final Flow flow;
    private String description;
    private Action request;
    private Action response;
    private Action error;
    private Status status;
    private String lifeCycle;

    public Version(Flow flow) {
        this.flow = flow;
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
}