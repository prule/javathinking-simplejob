package com.javathinking.batch.job;

/**
 * @author paul
 */
public interface Task {
    TaskResult process(Object obj);

    String getName();

    String getDiscriminator();
}
