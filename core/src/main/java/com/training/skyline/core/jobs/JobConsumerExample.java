package com.training.skyline.core.jobs;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(
        service = JobConsumer.class,
        immediate = true,
        property = {
                Constants.SERVICE_DESCRIPTION + "=Skyline Job Consumer",
                JobConsumer.PROPERTY_TOPICS + "=practice/job"
        }
)
public class JobConsumerExample implements JobConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobConsumerExample.class);

    @Override
    public JobResult process(Job job) {

        LOGGER.info("Job processing started for topic: {}", job.getTopic());

        try {
            // ✅ Read properties safely
            String data = (String) job.getProperty("data");
            String path = (String) job.getProperty("path");
            Long timestamp = (Long) job.getProperty("timestamp");

            // ✅ Validation
            if (path == null || path.isEmpty()) {
                LOGGER.warn("Invalid job payload: path is missing");
                return JobResult.CANCEL; // no retry
            }

            LOGGER.info("Processing job with data: {}, path: {}, timestamp: {}", data, path, timestamp);

            // ✅ Simulate business logic
            processContent(path);

            LOGGER.info("Job processed successfully for path: {}", path);

            return JobResult.OK;

        } catch (Exception e) {
            LOGGER.error("Error processing job. Will retry...", e);

            // ✅ Retry enabled
            return JobResult.FAILED;
        }
    }

    private void processContent(String path) {
        // Add your real logic here:
        // - Page update
        // - Asset processing
        // - API call
        LOGGER.debug("Executing business logic for path: {}", path);
    }
}