package com.training.skyline.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowProcess;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.Replicator;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = WorkflowProcess.class, property = {
        "process.label=Custom Activate Workflow"
})
public class ActivateCF implements WorkflowProcess {

    @Reference
    private Replicator replicator;

    private final WorkflowHelper workflowHelper = new WorkflowHelper();

    @Override
    public void execute(WorkItem workItem,
                        WorkflowSession workflowSession,
                        MetaDataMap metaDataMap) throws WorkflowException {

        if (replicator == null) {
            throw new WorkflowException("Replicator service is not available");
        }

        workflowHelper.payloadPublishProcess(
                workItem,
                workflowSession,
                metaDataMap,
                ReplicationActionType.ACTIVATE,
                replicator
        );
    }
}