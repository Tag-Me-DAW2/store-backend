package com.tagme.tagme_store_back.domain.validation;

import com.tagme.tagme_store_back.domain.exception.ValidationException;
import jakarta.validation.*;

import java.util.Set;

public class DtoValidator {
    private static Validator validator;

    private static Validator getValidator() {
        if (validator == null) {
            ValidatorFactory factory = Validation.byDefaultProvider()
                    .configure()
                    .buildValidatorFactory();
            validator = factory.getValidator();
        }
        return validator;
    }

    public static <T> void validate(T dto) {
        Set<ConstraintViolation<T>> violations = getValidator().validate(dto);
        if (!violations.isEmpty()) {
            throw new ValidationException(violations);
        }
    }
}
