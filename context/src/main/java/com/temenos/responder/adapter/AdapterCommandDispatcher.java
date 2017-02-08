package com.temenos.responder.adapter;

import com.google.inject.Singleton;
import com.temenos.responder.context.ExecutionContext;
import com.temenos.responder.context.FlowException;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class AdapterCommandDispatcher {
    private final Map<String, AdapterCommand> adapters = new HashMap<>();

    public <P extends AdapterParameters, R extends AdapterResult> R invokeAdapter(Class<P> adapterType, P adapterParameters, ExecutionContext executionContext) throws AdapterException {
        try {
            final AdapterContext<P> adapterContext = AdapterContext.<P>builder()
                    .parameters(adapterParameters)
                    .attributes(executionContext.getAttributes())
                    .headers(executionContext.getHeaders())
                    .queryParameters(executionContext.getQueryParameters())
                    .languagePreference(executionContext.getLanguagePreference())
                    .principal(executionContext.getSecurityContext().getPrincipal())
                    .build();
            return adapters.get(adapterType.getSimpleName()).execute(adapterContext);
        } catch (AdapterException e) {
            throw new FlowException(convertAdapterToFlowErrorCode(e.getFailureType()), e.getMessage(), e.getCode());
        } catch (Exception e) {
            throw new FlowException(500, e.getMessage(), null);
        }
    }

    private int convertAdapterToFlowErrorCode(AdapterFailureType failureType) {
        switch (failureType) {
            case CLIENT_ERROR: return 400;
            case SERVER_ERROR: return 500;
            case SECURITY_ERROR: return 401;
            case CONNECTION_ERROR: return 503;
            default: return 500;
        }
    }
}
