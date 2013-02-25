package com.javathinking.batch.support;

/**
 * @author paul
 */
public class MemoryServiceImpl implements MemoryService {
    public boolean hasAvailable(Long mem) {
        return mem == null || Runtime.getRuntime().freeMemory() >= mem;
    }
}
