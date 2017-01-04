package com.temenos.responder.mapper

import com.temenos.responder.commands.Command
import com.temenos.responder.flows.Flow
import com.temenos.responder.scaffold.Scaffold
import com.temenos.responder.configuration.HttpMethods
import com.temenos.responder.configuration.Resource

/**
 * Created by Douglas Groves on 09/12/2016.
 */
class ResourceMapper {
    //TODO: this method is too slow
    def map(resourceDef) {
        def resourceList = []
        resourceDef.resources.each { resource ->
            resource.each { name, defn ->
                HttpMethods.enumConstants.each { httpMethod ->
                    if(httpMethod.value in defn.directive){
                        resourceList.add(new Resource(
                                defn.path,
                                name,
                                httpMethod.value,
                                getModel(defn, httpMethod.value) as Class<Scaffold>,
                                getModelFromResponseCode(defn, httpMethod.value),
                                loadMeA(defn.directive."${httpMethod.value}".workflow)?.newInstance() as Flow,
                                defn.scope
                        ))
                    }
                }
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

    def getModel(defn, httpMethod){
        return (loadMeA(defn.directive."${httpMethod}".item) ?: loadMeA(defn.directive."${httpMethod}".collection))
    }

    def getModelFromResponseCode(defn, httpMethod){
        def responseCodeMap = [:]
        for(responseCode in defn.directive."${httpMethod}".responses){
            responseCodeMap["${responseCode.key}"] = (loadMeA(defn.directive."${httpMethod}".responses."${responseCode.key}".item) ?: loadMeA(defn.directive."${httpMethod}".responses."${responseCode.key}".collection))
        }
        return responseCodeMap
    }

    def loadMeA(what){
        if(!what){
            return null
        }else {
            return this.class.classLoader.loadClass(what)
        }
    }
}
