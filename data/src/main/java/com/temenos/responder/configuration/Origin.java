package com.temenos.responder.configuration;

import com.temenos.responder.flows.Flow;

import java.util.Objects;

/**
 * Instances of this class give details about the origin of a request to execute a flow for auditing purposes.
 *
 * @author Douglas Groves
 */
public final class Origin {

    private final Class<?> className;
    private final Thread thread;
    private final long flowId;

    private static final short NOT_A_FLOW = -1;

    public Origin(Class<? extends Flow> flowClassName, Thread thread){
        this(flowClassName, thread, NOT_A_FLOW);
    }

    Origin(Class<?> flowClassName, Thread thread, long flowId){
        this.className = flowClassName;
        this.thread = thread;
        this.flowId = flowId;
    }

    public Thread getThread() {
        return thread;
    }

    public Class<?> getClassName() {
        return className;
    }

    public long getFlowId() {
        return flowId;
    }

    public boolean originatesFromFlow(){
        return flowId != NOT_A_FLOW;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Origin origin = (Origin) o;
        return flowId == origin.flowId &&
                Objects.equals(className, origin.className) &&
                Objects.equals(thread, origin.thread);
    }

    @Override
    public int hashCode() {
        return Objects.hash(className, thread, flowId);
    }
}
