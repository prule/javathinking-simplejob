package com.javathinking.batch.support;

import java.io.File;
import java.io.FileFilter;

/**
 * @author paul
 */
public class ExtensionFileFilter implements FileFilter {
    private String[] extensions;

    public ExtensionFileFilter(String... extensions) {
        this.extensions = extensions;
    }

    @Override
    public boolean accept(File file) {
        for (String ext : extensions) {
            if (file.getName().toLowerCase().endsWith('.' + ext.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
