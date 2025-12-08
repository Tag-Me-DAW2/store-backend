package com.tagme.tagme_store_back.domain.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.validator.routines.EmailValidator;

public class EmailValidatorClass implements ConstraintValidator<ValidEmail, String> {

    private final EmailValidator commonsEmailValidator =
            EmailValidator.getInstance(false, false);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        return commonsEmailValidator.isValid(value);
    }
}

