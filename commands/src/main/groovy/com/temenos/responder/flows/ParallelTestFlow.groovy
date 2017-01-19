package com.temenos.responder.flows

import com.temenos.responder.context.ExecutionContext
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
        Map<String, List<Document>> results = context.notifyDispatchers([VersionInformationFlow, AdditionFlow])
        context.setAttribute("finalResult", new Entity([
                (ScaffoldParallelTestOutput.ADDITION_RESULT): results[AdditionFlow.simpleName][0].getBody().get(ScaffoldAdditionOutput.RESULT),
                (ScaffoldParallelTestOutput.VERSION)        : results[VersionInformationFlow.simpleName][0].getBody().get(ScaffoldVersion.VERSION_NUMBER)
        ]))
    }
}
