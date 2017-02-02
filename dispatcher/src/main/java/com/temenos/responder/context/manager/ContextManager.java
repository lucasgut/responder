package com.temenos.responder.context.manager;

import com.temenos.responder.context.Context;
import com.temenos.responder.context.ExecutionContext;

/**
 *
 *
 * @author Douglas Groves
 */
public interface ContextManager {
    long manageContext(Context context);
    <C extends Context> C getManagedContext(long contextId, Class<C> contextType);
    void deleteManagedContext(long contextId);
    void clearAllManagedContexts();
}
