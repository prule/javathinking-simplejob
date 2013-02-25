package com.javathinking.batch.example;

import com.javathinking.batch.event.Event;
import com.javathinking.batch.event.EventService;
import com.javathinking.batch.job.AbstractTask;
import com.javathinking.batch.job.Queue;
import com.javathinking.batch.job.TaskResult;
import com.javathinking.batch.job.TaskResult.Result;
import com.javathinking.commons.ZipUtil;
import org.apache.log4j.Logger;

import java.io.File;

/**
 * @author paul
 */
public class UnzipTask extends AbstractTask {
    private static final Logger log = Logger.getLogger(UnzipTask.class);
    private static int count = 1;
    private EventService eventService;
    private File workingDir;

    public UnzipTask(String discriminator, String name, File workingDir) {
        super(discriminator, name);
        this.workingDir = workingDir;
    }

    @Override
    public TaskResult process(Object obj) {
        Queue queue = (Queue) obj;
        File file = new File((String) queue.getData());
        File dest = getDestDir(workingDir);
        log.debug("Unzipping " + file.getAbsolutePath() + " to " + dest.getAbsolutePath());
        ZipUtil.unzip(file, dest);
        // add to queue
        eventService.send(new Event(getName(), file.getAbsolutePath() + " (unzipped to " + dest.getAbsolutePath() + ")", this.getDiscriminator()));
        return new TaskResult(Result.SUCCESS, dest.getAbsolutePath());
    }

    private synchronized File getDestDir(File dir) {
        boolean exists = true;
        File f = null;
        while (exists) {
            f = new File(dir, Integer.toString(count++));
            exists = f.exists();
        }
        f.mkdirs();
        return f;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }


}
