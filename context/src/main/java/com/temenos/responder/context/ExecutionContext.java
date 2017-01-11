package com.temenos.responder.context;

import com.temenos.responder.commands.Command;
import com.temenos.responder.entity.runtime.Entity;

/**
 * An ExecutionContext is a {@link Context context} that contains a map of attributes, request details and
 * {@link com.temenos.responder.configuration.Resource resource} details used during
 * {@link com.temenos.responder.flows.Flow flow} execution.
 *
 * @author Douglas Groves
 */
public interface ExecutionContext extends Context {
    /**
     * Returns the absolute URL used to request the {@link com.temenos.responder.configuration.Resource resource}.
     *
     * @return A string representation of the absolute URL of the resource that was requested.
     */
    String getSelf();

    /**
     * Returns the name of the {@link com.temenos.responder.configuration.Resource resource} that was requested.
     *
     * @return The name of the {@link com.temenos.responder.configuration.Resource resource} that was requested.
     */
    String getResourceName();

    /**
     * Fetch a configured {@link com.temenos.responder.commands.Command command} singleton via its Guice module.
     *
     * @param clazz The class name of the {@link com.temenos.responder.commands.Command command}.
     * @return A {@link com.temenos.responder.commands.Command command} singleton object.
     */
    Command getCommand(Class<Command> clazz);

    /**
     * Fetch a map of {@link com.temenos.responder.configuration.Resource resource} parameter names and values that
     * were resolved from the request URL.
     *
     * @return {@link com.temenos.responder.configuration.Resource Resource} parameter names and values resolved
     * with the request URL.
     */
    Parameters getParameters();

    /**
     * Fetch the contents of the request payload deserialised as an {@link Entity object}.
     *
     * @return The request payload deserialised as an {@link Entity object}.
     */
    Entity getRequestBody();

    /**
     * Obtain the value of a field inside the request body.
     *
     * @param fieldName The name of the field.
     * @return The value of the field that was fetched.
     */
    Object getFieldFromRequestBody(String fieldName);

    /**
     * Set the response code of the {@link com.temenos.responder.flows.Flow flow} based on the state the
     * transaction was left in following the last {@link Command command} that was executed.
     *
     * @param code A string representation of a response code.
     */
    void setResponseCode(String code);

    /**
     * Obtain the response code of the {@link com.temenos.responder.flows.Flow flow} that was executed.
     *
     * @return A string representation of the response code.
     */
    String getResponseCode();
}
