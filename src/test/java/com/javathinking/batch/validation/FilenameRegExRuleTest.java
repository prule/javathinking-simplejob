package com.javathinking.batch.validation;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author paul
 */
public class FilenameRegExRuleTest {
    @Test
    public void validate() {
        FilenameRegExRule rule = new FilenameRegExRule("(?i)[a-z]+\\.xml$");
        assertFalse(rule.validate(new File("h.zip")).isEmpty());
        assertTrue(rule.validate(new File("hh.xml")).isEmpty());
        assertTrue(rule.validate(new File("hh.XML")).isEmpty());
        assertTrue(rule.validate(new File("h.XmL")).isEmpty());
        assertFalse(rule.validate(new File("asdf.xml.txt")).isEmpty());
    }

}
