package com.javathinking.batch.job;

import com.javathinking.batch.job.TaskResult.Result;

import java.util.List;

/**
 * @author paul
 */
public interface QueueService {

    void add(String type, String discriminator, Object data, Result successful, Queue triggeredBy);

    List<Queue> list(Task task, Task dependsOn, Result dependsOnResult);

    void remove(Queue queue);

}
