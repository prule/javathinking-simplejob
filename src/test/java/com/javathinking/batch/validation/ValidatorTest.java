package com.javathinking.batch.validation;

import com.javathinking.commons.validation.Errors;
import com.javathinking.commons.validation.ValidationError;
import com.javathinking.commons.validation.ValidationRule;
import com.javathinking.commons.validation.Validator;
import org.junit.Before;
import org.junit.Test;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author paul
 */
public class ValidatorTest {

    Errors errorsSuccess;
    Errors errorsFail;
    ValidationRule rule1, rule2, rule3;

    @Before
    public void setup() {
        errorsSuccess = new Errors();
        errorsFail = new Errors();
        errorsFail.add(new ValidationError(this.getClass().getSimpleName(), "error"));

        rule1 = createMock(ValidationRule.class);
        rule2 = createMock(ValidationRule.class);
        rule3 = createMock(ValidationRule.class);

    }

    @Test
    public void validateWithoutFailFast() {

        expect(rule1.validate(anyObject())).andReturn(errorsSuccess).atLeastOnce();
        expect(rule2.validate(anyObject())).andReturn(errorsFail).atLeastOnce();
        expect(rule3.validate(anyObject())).andReturn(errorsSuccess).atLeastOnce();

        replay(rule1, rule2, rule3);

        Errors errors = doValidation(false, rule1, rule2, rule3);

        assertFalse(errors.isEmpty());
        verify(rule1, rule2, rule3);
    }

    @Test
    public void validateWithFailFast() {
        expect(rule1.validate(anyObject())).andReturn(errorsSuccess).atLeastOnce();
        expect(rule2.validate(anyObject())).andReturn(errorsFail).atLeastOnce();

        replay(rule1, rule2, rule3);

        Errors errors = doValidation(true, rule1, rule2, rule3);

        assertFalse(errors.isEmpty());
        verify(rule1, rule2, rule3);
    }

    @Test
    public void validateWithoutFail() {
        expect(rule1.validate(anyObject())).andReturn(errorsSuccess).atLeastOnce();
        expect(rule2.validate(anyObject())).andReturn(errorsSuccess).atLeastOnce();
        expect(rule3.validate(anyObject())).andReturn(errorsSuccess).atLeastOnce();

        replay(rule1, rule2, rule3);

        Errors errors = doValidation(true, rule1, rule2, rule3);

        assertTrue(errors.isEmpty());
        verify(rule1, rule2, rule3);
    }

    private Errors doValidation(boolean failfast, ValidationRule... rules) {
        Validator validator = new Validator(failfast);
        for (ValidationRule rule : rules) {
            validator.add(rule);
        }

        return validator.validate(new Object());
    }
}
