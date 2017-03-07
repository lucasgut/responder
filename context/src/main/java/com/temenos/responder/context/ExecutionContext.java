package com.temenos.responder.context;

import com.google.common.collect.ImmutableMap;
import com.temenos.responder.adapter.AdapterCommandDispatcher;
import com.temenos.responder.commands.Command;
import com.temenos.responder.context.builder.ExecutionParameterBuilder;
import com.temenos.responder.entity.runtime.Entity;
import com.temenos.responder.flows.Flow;

import java.util.List;
import java.util.Map;

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

    String getInternalResource(String resourcePath);

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
    <T extends Command> Command getCommand(Class<T> clazz);

    /**
     * Fetch the contents of the request payload deserialised as an {@link com.temenos.responder.entity.runtime.Entity object}.
     *
     * @return The request payload deserialised as an {@link com.temenos.responder.entity.runtime.Entity object}.
     */
    com.temenos.responder.entity.runtime.Entity getRequestBody();

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
     * @param code An integer representation of a response code.
     */
    void setResponseCode(int code);

    /**
     * Obtain the response code of the {@link com.temenos.responder.flows.Flow flow} that was executed.
     *
     * @return An integer representation of the response code.
     */
    int getResponseCode();

    /**
     * Notify a dispatcher that a flow is available to use and map the result of the operation to the given
     * context attribute name.
     *
     * @param targetFlow
     * @param parameters
     * @param into
     */
    void executeFlow(Class<? extends Flow> targetFlow, Parameters parameters, String into);

    /**
     * Notify a dispatcher that multiple flows are available to use and map the results of the operations to the given
     * context attribute name.
     *
     * @param targetFlows
     * @param parameters
     * @param into
     */
    void executeFlows(List<Class<? extends Flow>> targetFlows, List<Parameters> parameters, List<String> into);

    ExecutionParameterBuilder buildExecution();

    Class<? extends Flow> getFlowClass();
    FlowInvoker flow(String name);
    FlowDispatcher getFlowDispatcher();

    void setFlowParameters(Map<String, Object> flowParameters);
    Object getFlowParameter(String parameterName);
    String getFlowParameterAsString(String parameterName);
    Entity getFlowParameterAsEntity(String parameterName);

    String getQueryParameter(String parameterName);

    AdapterInvoker useAdapter(String adapterCommand);
    AdapterCommandDispatcher getAdapterDispatcher();

    List<Link> createLinks(String targetResource, String relation, ImmutableMap<String, Object> linkParameters);
}

