package com.temenos.responder.loader

import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by Douglas Groves on 08/12/2016.
 */
class ClasspathScriptLoaderTest extends Specification {
    def "Loader.loadScript() reads a single file from the classpath and returns a string"(scriptName, expectedResult) {
        setup:
            def loader = new ClasspathScriptLoader('resources')
        when:
            def result = loader.load(scriptName)
        then:
            result == expectedResult
        where:
            scriptName                    | expectedResult
            'resources/1_res/script.json' | '{"greeting": "Hello","subject": "World"}'
    }

    @Unroll
    def "Loader.loadScript() throws #exception if #condition"(scriptName, exception, condition) {
        setup:
            def loader = new ClasspathScriptLoader('resources')
        when:
            loader.load(scriptName)
        then:
            thrown(exception)
        where:
            scriptName                          | exception   | condition
            'resources/1_res/doesnt_exist.json' | IOException | 'the resource doesn\'t exist'
            'resources'                         | IOException | 'a directory is given'
    }

    @Unroll
    def "Loader.loadAllScripts() reads #size script files from directory #directory \
        and returns a list with #size strings totaling #contentSize characters"(size, directory, contentSize) {
        setup:
            def loader = new ClasspathScriptLoader(directory)
        when:
            def result = loader.loadAll()
        then:
            result.size() == size
            getContentSize(result) == contentSize
        where:
            size | directory             | contentSize
            1    | 'resources/1_res'     | 40
            2    | 'resources/2_res'     | 80
            3    | 'resources/3_res'     | 121
            3    | 'resources/cmplx_res' | 121
    }

    @Unroll
    def "Loader.loadAllScripts() throws #exception if #condition"(scriptName, exception, condition, message) {
        setup:
            def loader = new ClasspathScriptLoader(scriptName)
        when:
            loader.loadAll()
        then:
            def exceptionThrown = thrown(exception)
            exceptionThrown.message == message

        where:
            scriptName               | exception   | condition                      | message
            'resources/doesnt_exist' | IOException | 'the directory doesn\'t exist' | 'Directory resources/doesnt_exist does not exist.'
    }

    private def getContentSize(Map<String, String> content) {
        def contentSize = 0
        content.each { key, value ->
            contentSize += value.length()
        }
        return contentSize
    }
}
