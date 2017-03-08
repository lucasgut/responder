package com.temenos.responder.entity.metadata.t24entity;

import com.temenos.responder.entity.metadata.t24entity.aa.T24AAPropertyMetadata;
import com.temenos.responder.entity.metadata.t24entity.field.T24ApplicationFieldMetadata;

import java.util.Map;

public class T24AAActivityMetadata extends T24VersionMetadata {
    private String activityName;                                            // Activity Name
    private String productName;                                             // Product Name
    private String effectiveDate;                                           // Effective Date
    private Map<String, T24AAPropertyMetadata> aaProperties;                // AA property name to AA property metadata
    private Map<String, T24ApplicationFieldMetadata> fields;                // AA field name to Fields metadata
}
