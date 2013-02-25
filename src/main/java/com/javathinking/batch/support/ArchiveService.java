package com.javathinking.batch.support;

import java.io.File;

/**
 * @author paul
 */
public interface ArchiveService {

    /**
     * Move a file into the archive directory
     */
    File archive(File file);

}
