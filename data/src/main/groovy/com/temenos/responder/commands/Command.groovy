package com.temenos.responder.commands

import com.temenos.responder.context.ExecutionContext

/**
 * Created by Douglas Groves on 09/12/2016.
 */
interface Command {
    def execute(ExecutionContext context)
}