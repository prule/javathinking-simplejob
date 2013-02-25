package com.javathinking.batch.job;

import com.javathinking.batch.job.TaskResult.Result;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

/**
 * @author paul
 */
public class QueueServiceImpl implements QueueService {
    private static final Logger log = Logger.getLogger(QueueServiceImpl.class);
    @Autowired
    private QueueDao queueDao;

    public void remove(Queue queue) {
        queueDao.delete(queue);
    }

    public void add(String type, String discriminator, Object data, Result result, Queue triggeredBy) {
        queueDao.save(new Queue(type, discriminator, data, result, triggeredBy));
    }

    // TODO this could be made more efficient by using one sql query
    public List<Queue> list(Task task, Task dependsOn, Result dependsOnResult) {
        List<Queue> depends = queueDao.list(dependsOn.getName(), dependsOn.getDiscriminator(), dependsOnResult);
        if (!depends.isEmpty()) {
            List<Queue> alreadyDone = queueDao.list(task.getName(), task.getDiscriminator(), null);
            for (Queue queue1 : alreadyDone) {
                for (Queue queue2 : depends) {
                    if (queue2.getId().equals(queue1.getTriggeredBy())) {
                        depends.remove(queue2);
                        break;
                    }
                }
            }
            log.debug("Found " + depends.size() + " (" + dependsOn.getName() + " / " + dependsOn.getDiscriminator() + " / " + dependsOnResult + ") to be processed by " + task.getName() + " / " + task.getDiscriminator());
            return depends;
        }
        return Collections.EMPTY_LIST;
    }
}
