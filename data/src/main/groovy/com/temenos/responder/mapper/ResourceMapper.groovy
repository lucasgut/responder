package com.temenos.responder.mapper

import com.temenos.responder.entity.configuration.Resource

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class ResourceMapper {
    def map(resourceDef) {
        def resourceList = []
        resourceDef.resources.each { resource ->
            resource.each { name, defn ->
                resourceList.add(new Resource(
                        defn.path,
                        name,
                        [:],
                        null,
                        defn.scope
                ))
            }
        }
        return resourceList
    }

    def mapAll(resources) {
        def resourceList = []
        resources.each { resource ->
            resourceList += map(resource)
        }
        return resourceList
    }
}
