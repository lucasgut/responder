package com.temenos.responder.entity.metadata.t24entity.aa;

import com.temenos.responder.entity.metadata.t24entity.field.T24ApplicationFieldMetadata;

import java.util.Map;

public class T24AAVersionFieldMetadata {
    private String name;                                         // version name
    private Map<String, T24ApplicationFieldMetadata> fields;     // AA property field name to Fields metadata
}
