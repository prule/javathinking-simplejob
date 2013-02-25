package com.javathinking.batch.event;

/**
 * @author paul
 */
public interface NotificationHandler<T> {
    boolean process(T notification);
}
