package com.javathinking.batch.job;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author paul
 */
public class DirectoryMonitor implements Monitor<File> {

    private File directory;
    private String[] extensions;
    private Comparator<File> comparator;
    private boolean recursive;

    public DirectoryMonitor(File directory, String[] extensions, Comparator<File> comparator, boolean recursive) {
        this.directory = directory;
        this.extensions = extensions;
        this.comparator = comparator;
        this.recursive = recursive;
        this.directory.mkdirs();
    }

    public List<File> list() {
        List<File> files = new ArrayList<File>(FileUtils.listFiles(directory, extensions, recursive));

        if (comparator != null) {
            Collections.sort(files, comparator);
        }
        return files;
    }
}
