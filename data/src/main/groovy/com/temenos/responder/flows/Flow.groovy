package com.temenos.responder.flows

import com.temenos.responder.context.ExecutionContext

/**
 * Created by Douglas Groves on 04/01/2017.
 */
interface Flow {
    def execute(ExecutionContext context)
}