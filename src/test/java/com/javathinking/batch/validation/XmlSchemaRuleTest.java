package com.javathinking.batch.validation;

import com.javathinking.commons.test.TestUtil;
import com.javathinking.commons.validation.Errors;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author paul
 */
public class XmlSchemaRuleTest {

    @Test
    public void validate() {
        XmlSchemaRule rule = new XmlSchemaRule(TestUtil.getFile(this, "com/javathinking/batch/validation/shiporder.xsd"));
        Errors errors1 = rule.validate(TestUtil.getFile(this, "com/javathinking/batch/validation/shiporder.xml"));
        assertTrue(errors1.isEmpty());

        Errors errors2 = rule.validate(TestUtil.getFile(this, "com/javathinking/batch/validation/shiporder.invalid.xml"));
        assertFalse(errors2.isEmpty());

    }
}
