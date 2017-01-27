package com.temenos.responder.engine

import com.temenos.responder.exception.ScriptExecutionException
import com.temenos.responder.loader.ScriptLoader
import com.temenos.responder.scripting.GroovyScriptingEngine
import com.temenos.responder.scripting.ScriptingEngine
import spock.lang.Specification
import spock.lang.Unroll

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager

/**
 * Created by Douglas Groves on 07/12/2016.
 */
class GroovyScriptingEngineTest extends Specification {

    @Unroll
    def "Throw ScriptExecutionException with message: '#message' if '#qualifiedScriptName' does not exist"(qualifiedScriptName, scriptName, message) {
        setup:
            ScriptLoader loader = Mock(ScriptLoader)
            ScriptEngine scriptExecutor = new ScriptEngineManager().getEngineByName("groovy")
            def engine = new GroovyScriptingEngine(scriptExecutor, loader)
        when:
            1 * loader.load(_) >> { throw new IOException() }
            engine.execute(scriptName, 'method', null)
        then:
            def exception = thrown(ScriptExecutionException)
            exception.message == message
        where:
            qualifiedScriptName | scriptName | message
            'mistake.groovy'    | 'mistake'  | "Could not execute file mistake.groovy as a matching script file could not be found."
    }

    @Unroll
    def "Fetch '#script' from '#qualifiedScriptName', invoke '#method(#params)' and return '#data'"(qualifiedScriptName, scriptName, script, data, method, params) {
        setup:
            ScriptLoader loader = Mock(ScriptLoader)
            ScriptEngine scriptExecutor = new ScriptEngineManager().getEngineByName("groovy")
            def engine = new GroovyScriptingEngine(scriptExecutor, loader)
        when:
            1 * loader.load(scriptName + '.groovy') >> script
            def result = engine.execute(scriptName, method, params)
        then:
            result == data
        where:
            qualifiedScriptName | scriptName | script                                                           | data                                      | method  | params
            'example1.groovy'   | 'example1' | 'def hello(){ return ["Greeting": "Hello","Subject": "World"] }' | ["Greeting": "Hello", "Subject": "World"] | 'hello' | null
            'example1.groovy'   | 'example2' | 'def hello(){ return "Hello" }'                                  | "Hello"                                   | 'hello' | null
            'example1.groovy'   | 'example3' | 'def add(x,y){ return x + y }'                                   | 3                                         | 'add'   | [1, 2]
    }

    def getScriptName(name) {
        return name + '.groovy'
    }
}
