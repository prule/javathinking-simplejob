package com.javathinking.batch.example;

import com.javathinking.batch.event.EmailNotification;
import com.javathinking.batch.event.EmailNotificationFactory;
import com.javathinking.batch.event.Event;
import com.javathinking.batch.event.EventService;
import com.javathinking.batch.job.AbstractTask;
import com.javathinking.batch.job.Queue;
import com.javathinking.batch.job.TaskResult;
import com.javathinking.batch.job.TaskResult.Result;
import com.javathinking.batch.validation.XmlSchemaRule;
import com.javathinking.commons.validation.Errors;
import com.javathinking.commons.validation.Validator;
import org.apache.commons.lang.SystemUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author paul
 */
public class ShipOrderProcessorTask extends AbstractTask {

    private static final Logger log = Logger.getLogger(ShipOrderProcessorTask.class);
    private static final String SHIP_ORDER_XSD = "com/javathinking/batch/example/shiporder.xsd";
    private Validator validator;
    private EventService eventService;
    private EmailNotificationFactory emailNotificationFactory;

    public ShipOrderProcessorTask(String discriminator, String name) {
        super(discriminator, name);
        validator = new Validator();
        validator.add(new XmlSchemaRule(this.getClass().getClassLoader().getResource(SHIP_ORDER_XSD)));
    }

    /**
     * Process a unzipped file, given the directory it was unzipped to.
     * <p/>
     * So, for each file in the directory we:
     * 1. validate the schema
     * 2. then process the data.
     * <p/>
     * This could be split into 2 tasks, but this implementation shows how these steps can easily be combined
     * if it makes sense to.
     * <p/>
     * Note that our previous validation checked that only one file was in the zip file, but this task is written
     * without that consideration - it will simply process all files.
     */
    public TaskResult process(Object obj) {
        Queue queue = (Queue) obj;
        File dir = new File((String) queue.getData());
        log.info("Processing files in " + dir.getAbsolutePath());
        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File f, String name) {
                return (name.toUpperCase().endsWith(".XML"));
            }
        });

        Errors errors = new Errors();
        List<File> failedFiles = new ArrayList();
        for (File file : files) {
            Errors errs = validator.validate(file);
            errors.add(errs);
            log.debug("XML schema validation for " + file + (errs.isEmpty() ? " successful" : " failed [" + errs.toString() + "]"));
            if (errs.isEmpty()) {
                // TODO process data
            } else {
                failedFiles.add(file);
            }
        }
        if (!errors.isEmpty()) {
            Event event = new Event(getName(), "Processing failed", getDiscriminator());
            EmailNotification notification = emailNotificationFactory.get(Example1.PROC_FAILED_EMAIL);
            notification.setContent("Processing failed for files in " + dir + " (" + errors.toString() + ")");
            for (File f : failedFiles) {
                notification.addAttachment(f.getName(), f);
            }
            event.addNotification(notification);
            eventService.send(event);
        } else {
            Event event = new Event(getName(), "Processing Successful", getDiscriminator());
            EmailNotification notification = emailNotificationFactory.get(Example1.PROC_SUCCESS_EMAIL);
            notification.setContent("Processing successful for files in " + dir + SystemUtils.LINE_SEPARATOR);
            event.addNotification(notification);
            eventService.send(event);
        }
        return new TaskResult(errors.isEmpty() ? Result.SUCCESS : Result.FAIL, dir.getAbsolutePath());
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    void setEmailNotificationFactory(EmailNotificationFactory emailNotificationFactory) {
        this.emailNotificationFactory = emailNotificationFactory;
    }
}
