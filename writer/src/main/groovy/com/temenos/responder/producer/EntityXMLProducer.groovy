package com.temenos.responder.producer

import com.temenos.responder.entity.runtime.Entity
import groovy.util.slurpersupport.GPathResult
import groovy.util.slurpersupport.NodeChild

/**
 * Convert between an XML document and an Entity model.
 *
 * @author Douglas Groves
 */
class EntityXMLProducer implements EntityProducer {
    @Override
    Entity deserialise(String input) {
        def xml = new XmlSlurper().parseText(input)
        def document = [:]
        xml.attributes().each { key, val ->
            document["${xml.name()}.${key}"] = val
        }
        document[(xml.name())] = traverse(xml)
        return new Entity(document);
    }

    private traverse(GPathResult parent){
        def document = [:]
        parent.children().each { NodeChild node ->
            if(node.children().size() && !document[node.name()]){
                node.attributes().each { key, val ->
                    document["${node.name()}.${key}"] = val
                }
                document[node.name()] = traverse(node)
            }
            //if there are duplicate tag names, convert the element into an list
            else if(node.children().size() && document instanceof Map && document[node.name()]) {
                node.attributes().each { key, val ->
                    document["${node.name()}.${key}"] = val
                }
                document = [[(node.name()):document[node.name()]]]
                document.add([(node.name()):traverse(node)])
            }
            //if we are working with a list, add the current element
            else if(node.children().size() && document instanceof List){
                def elementMap = [(node.name()):traverse(node)]
                node.attributes().each { key, val ->
                    elementMap["${node.name()}.${key}"] = val
                }
                document.add(elementMap)
            }
            //if there are no child elements
            else if(document instanceof Map && document[node.name()]){
                node.attributes().each { key, val ->
                    document["${node.name()}.${key}"] = val
                }
                document = [[(node.name()):document[node.name()]]]
                document.add([(node.name()):node.text()])
            }
            //if we are working with a list and there are no child elements
            else if(document instanceof List){
                def elementMap = [(node.name()):node.text()]
                node.attributes().each { key, val ->
                    elementMap["${node.name()}.${key}"] = val
                }
                document.add(elementMap)
            }
            //add the element, its attributes and its text to the map
            else{
                document[node.name()] = node.text()
                node.attributes().each { key, val ->
                    document["${node.name()}.${key}"] = val
                }
            }
        }
        return document
    }

    @Override
    String serialise(Entity input) {
        return null
    }
}
