package com.javathinking.batch.validation;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author paul
 */
public class FilenameRegExRuleTest {
    @Test
    public void whenAFileNameMatchesRegExValidationShouldPass() {
        FilenameRegExRule rule = new FilenameRegExRule("(?i)[a-z]+\\.xml$");
        assertThat(rule.validate(new File("hh.xml")).isValid(), is(true));
        assertThat(rule.validate(new File("hh.XML")).isValid(), is(true));
        assertThat(rule.validate(new File("h.XmL")).isValid(), is(true));
    }

    @Test
    public void whenAFileNameDoesNotMatchRegExValidationShouldFail() {
        FilenameRegExRule rule = new FilenameRegExRule("(?i)[a-z]+\\.xml$");
        assertThat(rule.validate(new File("h.zip")).isNotValid(), is(true));
        assertThat(rule.validate(new File("asdf.xml.txt")).isNotValid(), is(true));
    }

}
