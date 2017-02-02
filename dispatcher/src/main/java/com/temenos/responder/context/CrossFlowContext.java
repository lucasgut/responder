package com.temenos.responder.context;

import com.temenos.responder.configuration.Origin;

import java.util.List;
import java.util.Map;

/**
 * A CrossFlowContext object is used by a {@link com.temenos.responder.dispatcher.FlowDispatcher flow dispatcher} to
 * pass parameters into a {@link com.temenos.responder.flows.Flow flow}.
 *
 * @author Douglas Groves
 */
public interface CrossFlowContext extends ParameterisedContext {

    /**
     * Obtain details of the web resource, class and thread responsible for creating this context.
     *
     * @return The web resource, class and thread responsible for creating this context.
     */
    Origin getOrigin();

    /**
     * Fetch all declared parameters.
     *
     * @return A {@link List list} of parameter values.
     */
    Parameters parameters();

    /**
     * Fetch the declared context attribute names that will be used to store the results of the operations.
     *
     * @return The context attribute name to which the result is mapped.
     */
    List<String> into();
}
