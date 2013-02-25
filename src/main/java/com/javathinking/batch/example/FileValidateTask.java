package com.javathinking.batch.example;

import com.javathinking.batch.event.Event;
import com.javathinking.batch.event.EventService;
import com.javathinking.batch.job.AbstractTask;
import com.javathinking.batch.job.Queue;
import com.javathinking.batch.job.TaskResult;
import com.javathinking.commons.validation.Errors;
import com.javathinking.commons.validation.Validator;

import java.io.File;

/**
 * @author paul
 */
public class FileValidateTask extends AbstractTask {
    private Validator validator;
    private EventService eventService;

    FileValidateTask(String discriminator, String name, Validator validator) {
        super(discriminator, name);
        this.validator = validator;
    }

    @Override
    public TaskResult process(Object obj) {
        Queue queue = (Queue) obj;
        File file = new File((String) queue.getData());
        Errors errors = validator.validate(file);
        if (errors.isEmpty()) {
            eventService.send(new Event(getName(), file.getAbsolutePath() + " successfully validated", getDiscriminator()));
        } else {
            Event event = new Event(getName(), file.getAbsolutePath() + " failed validation", getDiscriminator());
            // TODO set email notification
//            event.setNotification(new EmailNotification());
            eventService.send(event);
        }

        return new TaskResult(errors.isEmpty(), file.getAbsolutePath());
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }


}
