package com.temenos.responder.flows

import com.temenos.responder.context.ExecutionContext
import com.temenos.responder.context.Parameters
import com.temenos.responder.entity.runtime.Document
import com.temenos.responder.entity.runtime.Entity
import com.temenos.responder.scaffold.ScaffoldAdditionOutput
import com.temenos.responder.scaffold.ScaffoldParallelTestOutput
import com.temenos.responder.scaffold.ScaffoldVersion

/**
 * Created by dgroves on 19/01/2017.
 */
class ParallelTestFlow extends AbstractFlow {

    @Override
    void doExecute(ExecutionContext context) {
        context.buildExecution()
                .flow(VersionInformationFlow)
                .into('version')
                .execution()
                .inParallelWith(AdditionFlow)
                .into('add')
                .execution()
                .execute();
        context.setAttribute("finalResult", new Entity([
                (ScaffoldParallelTestOutput.ADDITION_RESULT): context.getAttribute('add').getBody().get(ScaffoldAdditionOutput.RESULT),
                (ScaffoldParallelTestOutput.VERSION)        : context.getAttribute('version').getBody().get(ScaffoldVersion.VERSION_NUMBER)
        ]))
    }
}
