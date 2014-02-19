package com.javathinking.batch.validation;

import com.javathinking.commons.test.TestUtil;
import com.javathinking.commons.validation.Errors;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author paul
 */
public class XmlSchemaRuleTest {

    @Test
    public void aValidXmlFileShouldPassSchemaValidation() {
        // setup
        XmlSchemaRule rule = new XmlSchemaRule(TestUtil.getFile(this, "com/javathinking/batch/validation/shiporder.xsd"));
        // perform
        Errors errors = rule.validate(TestUtil.getFile(this, "com/javathinking/batch/validation/shiporder.xml"));
        // assert
        assertThat(errors.isValid(), is(true));
    }

    @Test
    public void anInvalidXmlFileShouldFailSchemaValidation() {
        // setup
        XmlSchemaRule rule = new XmlSchemaRule(TestUtil.getFile(this, "com/javathinking/batch/validation/shiporder.xsd"));
        // perform
        Errors errors = rule.validate(TestUtil.getFile(this, "com/javathinking/batch/validation/shiporder.invalid.xml"));
        // assert
        assertThat(errors.isNotValid(), is(true));

    }
}
