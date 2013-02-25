package com.javathinking.batch.validation;

import com.javathinking.commons.validation.Errors;
import com.javathinking.commons.validation.ValidationError;
import com.javathinking.commons.validation.ValidationRule;

import java.io.File;
import java.text.MessageFormat;

/**
 * @author paul
 */
public class FilenameRegExRule implements ValidationRule<File> {
    private String pattern;
    private static final String format = "File {0} did not match regular expression {1}";

    public FilenameRegExRule(String pattern) {
        this.pattern = pattern;
    }

    public Errors validate(File input) {
        Errors errors = new Errors();
        if (!input.getName().matches(pattern)) {
            errors.add(new ValidationError(this.getClass().getSimpleName(), MessageFormat.format(format, input.getName(), pattern)));
        }
        return errors;
    }


}
