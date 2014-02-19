package com.javathinking.batch.validation;

import com.javathinking.commons.validation.Errors;
import com.javathinking.commons.validation.ValidationError;
import com.javathinking.commons.validation.ValidationRule;

import java.io.File;
import java.text.MessageFormat;

/**
 * Checks that the given file has an extension that matches the defined extension.
 * This check can either be case sensitive or insensitive.
 */
public class FilenameExtensionRule implements ValidationRule<File> {
    private String extension;
    private boolean caseSensitive;

    private static final String format = "File {0} does not end with extension {1}";

    public FilenameExtensionRule(String extension, boolean caseSensitive) {
        this.extension = extension;
        this.caseSensitive = caseSensitive;
    }

    public Errors validate(File input) {
        Errors errors = new Errors();
        String ext = extension;
        String filename = input.getName();

        if (!caseSensitive) {
            ext = ext.toUpperCase();
            filename = filename.toUpperCase();
        }

        if (!filename.endsWith(ext)) {
            errors.add(new ValidationError(this.getClass().getSimpleName(), MessageFormat.format(format, input.getName(), extension)));
        }
        return errors;
    }


}
