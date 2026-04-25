package com.training.skyline.core.schedulers;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.sling.event.jobs.JobManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import uk.org.lidalia.slf4jext.Level;
import uk.org.lidalia.slf4jtest.LoggingEvent;
import uk.org.lidalia.slf4jtest.TestLogger;
import uk.org.lidalia.slf4jtest.TestLoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(AemContextExtension.class)
class SimpleScheduledTaskTest {

    private SimpleScheduledTask fixture;

    private TestLogger logger;

    private JobManager jobManager;

    @BeforeEach
    void setup() throws IllegalAccessException {
        fixture = new SimpleScheduledTask();

        logger = TestLoggerFactory.getTestLogger(SimpleScheduledTask.class);
        TestLoggerFactory.clear();

        jobManager = mock(JobManager.class);

        FieldUtils.writeField(fixture, "jobManager", jobManager, true);
    }

    @Test
    void testRun_ShouldAddJobAndLogMessages() {

        // Mock config
        SimpleScheduledTask.Config config = mock(SimpleScheduledTask.Config.class);
        when(config.myParameter()).thenReturn("parameter value");

        // Activate + run
        fixture.activate(config);
        fixture.run();

        // ✅ Verify job added
        verify(jobManager, times(1))
                .addJob(eq("practice/job"), anyMap());

        // ✅ Verify logs
        List<LoggingEvent> events = logger.getLoggingEvents();

        // Instead of exact count, check meaningful logs exist
        assertTrue(events.stream().anyMatch(e ->
                e.getLevel() == Level.INFO &&
                        e.getMessage().contains("Scheduler triggered")
        ));

        assertTrue(events.stream().anyMatch(e ->
                e.getLevel() == Level.INFO &&
                        e.getMessage().contains("Job successfully added")
        ));
    }
}