package com.javathinking.batch.support;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * A simple archive service that moves a given file into the configured archive directory,
 * categorising by YEAR/MONTH/COUNT where COUNT is the next highest available integer.
 * <p/>
 * The COUNT subdirectory is used to avoid filename clashes.
 *
 * @author paul
 */
public class ArchiveServiceImpl implements ArchiveService {

    private File archiveDir;

    public synchronized File archive(File file) {
        Calendar cal = Calendar.getInstance();

        File yFile = new File(archiveDir, Integer.toString(cal.get(Calendar.YEAR)));
        File ymFile = new File(yFile, Integer.toString(cal.get(Calendar.MONTH) + 1));

        int count = 1;
        File ymnFile = null;
        do {
            ymnFile = new File(ymFile, Integer.toString(count));
            count++;
        } while (ymnFile.exists());

        ymnFile.mkdirs();
        File destFile = new File(ymnFile, file.getName());
        try {
            FileUtils.moveFile(file, destFile);
            return destFile;
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setArchiveDir(File archiveDir) {
        this.archiveDir = archiveDir;
    }
}
