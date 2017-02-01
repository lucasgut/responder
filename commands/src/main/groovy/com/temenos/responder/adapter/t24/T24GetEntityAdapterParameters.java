package com.temenos.responder.adapter.t24;

import com.temenos.responder.adapter.AdapterParameters;
import lombok.Builder;

import javax.annotation.Nonnull;

@Builder
public class T24GetEntityAdapterParameters implements AdapterParameters {
    @Nonnull
    private final String t24EnquiryName;
    @Nonnull
    private final String id;
}
