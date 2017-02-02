package com.temenos.responder.adapter;

import com.temenos.responder.entity.runtime.Entity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdapterResult {
    private Entity entity;
    private int errorCode;
    private String errorMessage;

    public boolean isSuccess() {
        return errorCode != 0;
    }
}
