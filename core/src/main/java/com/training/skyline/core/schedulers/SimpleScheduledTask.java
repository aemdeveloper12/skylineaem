package com.training.skyline.core.schedulers;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Designate(ocd = SimpleScheduledTask.Config.class)
@Component(service = Runnable.class)
public class SimpleScheduledTask implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleScheduledTask.class);

    private static final String JOB_TOPIC = "practice/job";

    @Reference
    private JobManager jobManager;

    private String myParameter;

    @ObjectClassDefinition(
            name = "Skyline Scheduled Job",
            description = "Triggers Sling Job for background processing"
    )
    public @interface Config {

        @AttributeDefinition(name = "Cron Expression")
        String scheduler_expression() default "*/30 * * * * ?";

        @AttributeDefinition(name = "Allow Concurrent Execution")
        boolean scheduler_concurrent() default false;

        @AttributeDefinition(name = "Custom Parameter")
        String myParameter() default "";
    }

    @Activate
    protected void activate(Config config) {
        this.myParameter = config.myParameter();
        LOGGER.info("Scheduler activated with parameter: {}", myParameter);
    }

    @Override
    public void run() {

        LOGGER.info("Scheduler triggered. Preparing job...");

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("data", "test");
            payload.put("path", "/content/skyline/us/en");
            payload.put("timestamp", System.currentTimeMillis());

            jobManager.addJob(JOB_TOPIC, payload);

            LOGGER.info("Job successfully added to topic: {}", JOB_TOPIC);

        } catch (Exception e) {
            LOGGER.error("Error while adding job", e);
        }
    }
}