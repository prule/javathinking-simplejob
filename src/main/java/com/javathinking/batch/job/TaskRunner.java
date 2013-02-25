package com.javathinking.batch.job;

import com.javathinking.batch.job.TaskResult.Result;
import com.javathinking.batch.support.MemoryService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author paul
 */
public class TaskRunner {

    private static final Logger log = Logger.getLogger(TaskRunner.class);
    private List<Task> tasks = new ArrayList<Task>();
    private Monitor monitor;
    private QueueService queueService;
    private MemoryService memoryService;
    private Long minMemoryRequired = null;

    public TaskRunner(Monitor monitor, QueueService queueService) {
        this.monitor = monitor;
        this.queueService = queueService;
    }

    public boolean process() {
        if (memoryService == null || memoryService.hasAvailable(minMemoryRequired)) {
            List inputs = monitor.list();
            for (Object o : inputs) {
                if (!runTasks(o)) {
                    return false;
                }
            }
            return true;
        } else {
            log.warn("Unable to process task due to invalid free memory");
            return false;
        }
    }

    private boolean runTasks(Object input) {
        Queue triggeredBy = null;
        if (input instanceof Queue) {
            triggeredBy = (Queue) input;
        }
        for (Task task : tasks) {
            log.debug("Task starting : " + task.getName() + " / " + task.getDiscriminator());
            try {
                TaskResult result = task.process(input);
                log.debug("Task completed : " + task.getName() + " / " + task.getDiscriminator() + " / " + result.getResult().name());
                queueService.add(task.getName(), task.getDiscriminator(), result.getData(), result.getResult(), triggeredBy);
            } catch (Exception e) {
                log.error("Task completed in error: " + task.getName() + " / " + task.getDiscriminator() + " / " + e.toString());
                queueService.add(task.getName(), task.getDiscriminator(), e.toString(), Result.ERROR, triggeredBy);
            }
        }
        return true;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void setMinMemoryRequired(Long minMemoryRequired) {
        this.minMemoryRequired = minMemoryRequired;
    }

    public void setMemoryService(MemoryService memoryService) {
        this.memoryService = memoryService;
    }

}
