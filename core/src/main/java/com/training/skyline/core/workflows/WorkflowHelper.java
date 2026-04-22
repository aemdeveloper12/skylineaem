package com.training.skyline.core.workflows;

import com.adobe.granite.workflow.WorkflowException;
import com.adobe.granite.workflow.WorkflowSession;
import com.adobe.granite.workflow.exec.WorkItem;
import com.adobe.granite.workflow.exec.WorkflowData;
import com.adobe.granite.workflow.metadata.MetaDataMap;
import com.day.cq.replication.ReplicationActionType;
import com.day.cq.replication.ReplicationException;
import com.day.cq.replication.Replicator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import javax.jcr.Node;
import java.util.ArrayList;
import java.util.List;

public class WorkflowHelper {

    private static final Logger log = LoggerFactory.getLogger(WorkflowHelper.class);

    public void payloadPublishProcess(WorkItem workItem,
                                      WorkflowSession workflowSession,
                                      MetaDataMap metaDataMap,
                                      ReplicationActionType actionType,
                                      Replicator replicator) throws WorkflowException {

        log.info("Workflow Payload Publish Process - Start");

        try {
            WorkflowData workflowData = workItem.getWorkflowData();
            String payloadType = workflowData.getPayloadType();

            Session session = workflowSession.adaptTo(Session.class);

            if (session == null) {
                throw new WorkflowException("JCR Session is null");
            }

            String path = null;

            // ✅ Handle both payload types
            if ("JCR_PATH".equals(payloadType)) {
                path = workflowData.getPayload().toString();

            } else if ("JCR_UUID".equals(payloadType)) {
                String uuid = workflowData.getPayload().toString();
                Node node = session.getNodeByIdentifier(uuid);
                path = node.getPath();
            }

            if (StringUtils.isBlank(path)) {
                throw new WorkflowException("Payload path is empty");
            }

            log.info("Resolved payload path: {}", path);

            // ✅ Support multiple paths if needed later
            List<String> paths = new ArrayList<>();
            paths.add(path);

            for (String payloadPath : paths) {

                // ✅ Identify content type
                if (payloadPath.startsWith("/content/dam")) {
                    log.info("Processing Asset or Content Fragment: {}", payloadPath);

                } else if (payloadPath.startsWith("/content")) {
                    log.info("Processing Page: {}", payloadPath);

                } else {
                    log.warn("Skipping unsupported path: {}", payloadPath);
                    continue;
                }

                replicate(session, payloadPath, actionType, replicator);
            }

        } catch (Exception e) {
            log.error("Error in payloadPublishProcess", e);
            throw new WorkflowException(e);
        }

        log.info("Workflow Payload Publish Process - End");
    }

    public void replicate(Session session,
                          String path,
                          ReplicationActionType actionType,
                          Replicator replicator) throws WorkflowException {

        try {
            replicator.replicate(session, actionType, path);
            log.info("Successfully replicated: {} with action: {}", path, actionType);

        } catch (ReplicationException e) {
            log.error("Replication failed for path: {}", path, e);
            throw new WorkflowException(e);
        }
    }
}