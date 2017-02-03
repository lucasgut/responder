package com.temenos.responder.adapter.t24;

import com.temenos.responder.adapter.AdapterClient;
import lombok.Builder;

import javax.annotation.Nonnull;

@Builder
public class T24GetEntityAdapterClient implements AdapterClient {
    @Nonnull
    private final String t24EnquiryName;
    @Nonnull
    private final String id;
}
