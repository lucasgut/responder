package com.temenos.responder.mapper

import com.temenos.responder.commands.Command
import com.temenos.responder.commands.Scaffold
import com.temenos.responder.configuration.Resource

import javax.ws.rs.core.Response

/**
 * Created by Douglas Groves on 09/12/2016.
 */
//TODO: Where are the tests?
class ResourceMapper {
    def map(resourceDef) {
        def resourceList = []
        resourceDef.resources.each { resource ->
            resource.each { name, defn ->
                resourceList.add(new Resource(
                        defn.path,
                        name,
                        [200:loadMeA(defn.directive.GET.responses['200'].item)] as Map<Integer, Class<Scaffold>>,
                        loadMeA(defn.directive.GET.workflow)?.newInstance() as Command,
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

    def loadMeA(what){
        return this.class.classLoader.loadClass(what)
    }
}
