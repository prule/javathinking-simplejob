package com.javathinking.batch.example;

import com.javathinking.batch.event.Event;
import com.javathinking.batch.event.EventService;
import com.javathinking.batch.job.AbstractTask;
import com.javathinking.batch.job.TaskResult;
import com.javathinking.batch.support.ArchiveService;

import java.io.File;

/**
 * @author paul
 */
public class FileReceiveTask extends AbstractTask {
    private ArchiveService archiveService;
    private EventService eventService;

    public FileReceiveTask(String discriminator, String name) {
        super(discriminator, name);
    }

    @Override
    public TaskResult process(Object obj) {
        File file = (File) obj;
        File archivedFile = archiveService.archive(file);
        eventService.send(new Event(getName(), file.getAbsolutePath() + " (archived to " + archivedFile.getAbsolutePath() + ")", getDiscriminator()));
        return new TaskResult(true, archivedFile.getAbsolutePath());
    }

    public void setArchiveService(ArchiveService archiveService) {
        this.archiveService = archiveService;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }


}
