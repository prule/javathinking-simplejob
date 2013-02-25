package com.javathinking.batch.job;

import com.javathinking.batch.job.TaskResult.Result;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a collection of tasks. With this implementation, each task becomes its own thread - each task runs in parallel.
 *
 * @author paul
 */
public class ThreadedTaskManager {

    private static final Logger log = Logger.getLogger(ThreadedTaskManager.class);
    private boolean run = true;
    private QueueService queueService;
    private List<Thread> threads = new ArrayList<Thread>();

    /**
     * Pass multiple tasks here to have them run in just the one thread.
     */
    public void addTask(final long sleep, final Monitor monitor, final Task... tasks) {
        Runnable r = new Runnable() {

            @Override
            public void run() {
                TaskRunner runner = new TaskRunner(monitor, queueService);
                for (Task task : tasks) {
                    runner.addTask(task);
                }
                while (run) {
                    runner.process();
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        };
        Thread t = new Thread(r);
        threads.add(t);
        t.start();
    }

    public void addTask(long sleep, Task task, Task dependsOnTask, Result result) {
        addTask(sleep, new QueueMonitor(task, dependsOnTask, result, queueService), task);
    }

    public void shutdown() {
        run = false;
    }

    public void setQueueService(QueueService queueService) {
        this.queueService = queueService;
    }

}
