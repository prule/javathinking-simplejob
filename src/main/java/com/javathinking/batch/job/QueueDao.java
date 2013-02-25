package com.javathinking.batch.job;

import com.javathinking.batch.job.TaskResult.Result;

import java.util.List;

/**
 * @author paul
 */
public interface QueueDao {

    void delete(Queue queue);

    List<Queue> list(String type, String discriminator, Result result);

    void save(Queue queue);

}
