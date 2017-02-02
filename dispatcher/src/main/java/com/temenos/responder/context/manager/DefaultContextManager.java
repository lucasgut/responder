package com.temenos.responder.context.manager;

import com.temenos.responder.context.Context;
import com.temenos.responder.context.CrossFlowContext;
import com.temenos.responder.context.builder.CrossFlowContextBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;

/**
 * Created by dgroves on 31/01/2017.
 */
public class DefaultContextManager implements ContextManager {

    private Map<Long, Context> managedContextInstances;
    private AtomicLong nextId = new AtomicLong(INITIAL_ID);
    private Lock lock;

    private static DefaultContextManager INSTANCE;

    private static final long MAXIMUM_POOL_SIZE = 5;
    private static final long INITIAL_ID = 0;

    private DefaultContextManager(){
        this.managedContextInstances = new HashMap<>();
    }

    public long manageContext(Context context) {
        long id = nextId.incrementAndGet();
        managedContextInstances.put(id, context);
        return id;
    }

    @Override
    public <C extends Context> C getManagedContext(long contextId, Class<C> contextType) {
        return contextType.cast(managedContextInstances.get(contextId));
    }

    @Override
    public void deleteManagedContext(long contextId) {
        managedContextInstances.remove(contextId);
    }

    @Override
    public void clearAllManagedContexts() {
        managedContextInstances.clear();
    }

    public static DefaultContextManager getManager(){
        if(INSTANCE == null){
            INSTANCE = new DefaultContextManager();
        }
        return INSTANCE;
    }
}
