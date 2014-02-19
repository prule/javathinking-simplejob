package com.javathinking.batch.validation;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author paul
 */
public class FilenameExtensionRuleTest {
    @Test
    public void aFileWithCorrectExtensionShouldPassValidation() {
        FilenameExtensionRule rule = new FilenameExtensionRule(".txt", false);
        assertThat(rule.validate(new File("h.txt")).isValid(), is(true));
    }

    @Test
    public void aFileWithIncorrectExtensionShouldPassValidation() {
        FilenameExtensionRule rule = new FilenameExtensionRule(".txt", false);
        assertThat(rule.validate(new File("h.txtxxx")).isNotValid(), is(true));
    }

    @Test
    public void whenNotUsingCaseSensitiveCaseShouldNotMatter() {
        FilenameExtensionRule rule = new FilenameExtensionRule(".txt", false);
        assertThat(rule.validate(new File("h.TXT")).isValid(), is(true));
        assertThat(rule.validate(new File("h.txt")).isValid(), is(true));
        assertThat(rule.validate(new File("h.tXt")).isValid(), is(true));
    }

    @Test
    public void whenUsingCaseSensitiveCaseShouldMatter() {
        FilenameExtensionRule rule = new FilenameExtensionRule(".txt", true);
        assertThat(rule.validate(new File("h.TXT")).isNotValid(), is(true));
        assertThat(rule.validate(new File("h.txt")).isValid(), is(true));
    }
}
