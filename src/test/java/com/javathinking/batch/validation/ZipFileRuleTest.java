package com.javathinking.batch.validation;

import com.javathinking.commons.test.TestUtil;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author paul
 */
public class ZipFileRuleTest {
    @Test
    public void validate1() {
        ZipFileRule rule = new ZipFileRule(1, 1);
        File f = TestUtil.getFile(this, "com/javathinking/batch/validation/OneFile.zip");
        assertTrue(rule.validate(f).isEmpty());
    }

    @Test
    public void validateMin() {
        File f = TestUtil.getFile(this, "com/javathinking/batch/validation/ThreeFile.zip");

        ZipFileRule rule1 = new ZipFileRule(2, 4);
        assertTrue(rule1.validate(f).isEmpty());

        ZipFileRule rule2 = new ZipFileRule(3, 4);
        assertTrue(rule2.validate(f).isEmpty());

        ZipFileRule rule3 = new ZipFileRule(4, 10);
        assertFalse(rule3.validate(f).isEmpty());

    }

}
