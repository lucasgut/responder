package com.temenos.responder.mapper

import com.temenos.responder.entity.configuration.Resource

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class ResourceMapper {
    static def map(resourceDef){
        def resourceList = []
        resourceDef.resources.each { key, value ->
            resourceList += new Resource(
                value.path,
                key,
                [:],
                value.scope
            )
        }
        return resourceList
    }

    static def mapAll(resources){
        def resourceList = []
        resources.each { resource ->
            resourceList += map(resource)
        }
        return resourceList
    }
}
