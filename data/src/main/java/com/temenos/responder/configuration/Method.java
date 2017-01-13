package com.temenos.responder.configuration;

import java.util.List;

/**
 * Created by aburgos on 11/01/2017.
 */
public class Method {
    private final HttpMethod method;
    private int cacheSeconds;
    private String cacheReason;
    private String routeOn;
    private final List<Version> routeTo;

    public static final String CACHE_SECONDS = "cacheSeconds";
    public static final String CACHE_REASON = "cacheReason";
    public static final String ROUTE_ON = "routeOn";
    public static final String ROUTE_TO = "routeTo";

    public Method(HttpMethod method, List<Version> versions) {
        this.method = method;
        this.routeTo = versions;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public List<Version> getVersions() {
        return routeTo;
    }

    public void setCacheSeconds(int cacheSeconds) {
        this.cacheSeconds = cacheSeconds;
    }

    public int getCacheSeconds() {
        return cacheSeconds;
    }

    public void setCacheReason(String cacheReason) {
        this.cacheReason = cacheReason;
    }

    public String getCacheReason() {
        return cacheReason;
    }

    public void setRouteOn(String routeOn) {
        this.routeOn = routeOn;
    }

    public String getRouteOn() {
        return routeOn;
    }
}