package com.clodrock.sakabe.validator;

import com.clodrock.sakabe.enums.PropertyTypeEnum;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class PropertyValidator implements ConstraintValidator<ValidProperty, String> {
    @Override
    public void initialize(ValidProperty constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String propertyName, ConstraintValidatorContext constraintValidatorContext) {
        if (validateProperty(propertyName)){
            return Boolean.TRUE;
        }
        return false;
    }

    private boolean validateProperty(String propertyName) {
        return Arrays.stream(PropertyTypeEnum.values()).map(PropertyTypeEnum::name)
                .anyMatch(p -> p.equals(propertyName));
    }
}
