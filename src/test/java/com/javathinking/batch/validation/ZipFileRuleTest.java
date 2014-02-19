package com.javathinking.batch.validation;

import com.javathinking.commons.test.TestUtil;
import com.javathinking.commons.validation.Errors;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author paul
 */
public class ZipFileRuleTest {
    @Test
    public void aValidZipWithOneFileShouldPassValidation() {
        ZipFileRule rule = new ZipFileRule(1, 1);
        File f = TestUtil.getFile(this, "com/javathinking/batch/validation/OneFile.zip");
        Errors errors = rule.validate(f);
        assertThat(errors.isValid(), is(true));
    }

    @Test
    public void aValidZipWithThreeFilesShouldPassValidation() {
        File f = TestUtil.getFile(this, "com/javathinking/batch/validation/ThreeFile.zip");

        {
            ZipFileRule rule = new ZipFileRule(2, 4);
            Errors errors = rule.validate(f);
            assertThat(errors.isValid(), is(true));
        }
        {
            ZipFileRule rule = new ZipFileRule(3, 4);
            Errors errors = rule.validate(f);
            assertThat(errors.isValid(), is(true));
        }
    }

    @Test
    public void aValidZipViolatingMaxFilesShouldFailValidation() {
        File f = TestUtil.getFile(this, "com/javathinking/batch/validation/ThreeFile.zip");

        ZipFileRule rule = new ZipFileRule(0, 2);
        Errors errors = rule.validate(f);
        assertThat(errors.isNotValid(), is(true));
    }

    @Test
    public void aValidZipViolatingMinFilesShouldFailValidation() {
        File f = TestUtil.getFile(this, "com/javathinking/batch/validation/ThreeFile.zip");

        ZipFileRule rule = new ZipFileRule(4, 10);
        Errors errors = rule.validate(f);
        assertThat(errors.isNotValid(), is(true));

    }

}
