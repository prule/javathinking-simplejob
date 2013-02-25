package com.javathinking.batch.job;

import com.javathinking.batch.support.MemoryService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.easymock.EasyMock.*;

/**
 * @author paul
 */
public class TaskRunnerTest {
    MemoryService memoryService;
    Task task;
    Monitor monitor;
    QueueService queueService;
    TaskRunner runner;
    List<Queue> queues;

    @Before
    public void setup() {
        memoryService = createMock(MemoryService.class);
        task = createMock(Task.class);
        queueService = createNiceMock(QueueService.class);
        monitor = createMock(Monitor.class);
        queues = new ArrayList();
        queues.add(new Queue("a", "b", "data", TaskResult.Result.SUCCESS, null));
    }

    /**
     * Make sure that when there is not enough memory, the task doesn't get invoked
     */
    @Test
    public void testWithoutEnoughMemory() throws Exception {
        expect(memoryService.hasAvailable(anyLong())).andReturn(false).anyTimes();
        replay(memoryService, task, queueService, monitor);

        runner = new TaskRunner(monitor, queueService);
        runner.setMemoryService(memoryService);
        runner.addTask(task);
        Assert.assertFalse(runner.process());
        verify(task);
    }

    @Test
    public void testWithEnoughMemory() throws Exception {
        expect(task.process(anyObject())).andReturn(new TaskResult(true, null)).atLeastOnce();
        expect(task.getName()).andReturn("a").anyTimes();
        expect(task.getDiscriminator()).andReturn("b").anyTimes();
        expect(monitor.list()).andReturn(queues).atLeastOnce();
        expect(memoryService.hasAvailable(anyLong())).andReturn(true).atLeastOnce();
        replay(memoryService, task, queueService, monitor);

        runner = new TaskRunner(monitor, queueService);
        runner.setMemoryService(memoryService);
        runner.addTask(task);
        Assert.assertTrue(runner.process());
        verify(task);
    }

}
