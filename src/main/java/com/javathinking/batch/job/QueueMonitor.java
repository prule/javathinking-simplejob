package com.javathinking.batch.job;

import com.javathinking.batch.job.TaskResult.Result;

import java.util.List;

/**
 * @author paul
 */
class QueueMonitor implements Monitor<Queue> {
    private Task task;
    private Task dependsOn;
    private Result dependsOnResult;

    private QueueService queueService;


    public QueueMonitor(Task task, Task dependsOn, Result dependsOnResult, QueueService queueService) {
        this.task = task;
        this.dependsOn = dependsOn;
        this.dependsOnResult = dependsOnResult;
        this.queueService = queueService;
    }

    public List<Queue> list() {
        return queueService.list(task, dependsOn, dependsOnResult);
    }

}
