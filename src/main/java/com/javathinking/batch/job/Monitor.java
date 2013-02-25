package com.javathinking.batch.job;

import java.util.List;

/**
 * @author paul
 */
public interface Monitor<T> {
    public List<T> list();
}
