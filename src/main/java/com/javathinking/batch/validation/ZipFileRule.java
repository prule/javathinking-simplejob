package com.javathinking.batch.validation;

import com.javathinking.commons.ZipUtil;
import com.javathinking.commons.validation.Errors;
import com.javathinking.commons.validation.ValidationError;
import com.javathinking.commons.validation.ValidationRule;

import java.io.File;
import java.text.MessageFormat;

/**
 * @author paul
 */
public class ZipFileRule implements ValidationRule<File> {
    private static final String format1 = "Zip file {0} contained {1} files - expected {3} of {2}";
    private static final String format2 = "Zip file {0} contained is not a valid zip file";
    private int max;
    private int min;

    public ZipFileRule(int min, int max) {
        this.max = max;
        this.min = min;
    }

    public Errors validate(File input) {
        final Errors errors = new Errors();
        if (!ZipUtil.isValid(input)) {
            errors.add(new ValidationError(this.getClass().getSimpleName(), MessageFormat.format(format2, input.getName())));
            // can't continue processing a zip file that isn't a valid zip
            return errors;
        }
        int count = ZipUtil.countFiles(input);
        if (count > max) {
            errors.add(new ValidationError(this.getClass().getSimpleName(), MessageFormat.format(format1, input.getName(), count, max, "maximum")));
        }
        if (count < min) {
            errors.add(new ValidationError(this.getClass().getSimpleName(), MessageFormat.format(format1, input.getName(), count, min, "minimum")));
        }
        return errors;
    }

}
