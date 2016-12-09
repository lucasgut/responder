package com.temenos.responder.scripting;

import com.temenos.responder.exception.ScriptExecutionException;

import java.util.Collection;

public interface ScriptingEngine {
	Object execute(String scriptName, String methodName, Object... params) throws ScriptExecutionException;
}
