package com.antoinecampbell.cloud.springcloudfunction.item;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckItemValidator implements ConstraintValidator<CheckItem, Item> {

    @Override
    public boolean isValid(Item value, ConstraintValidatorContext context) {
        return !"bad name".equalsIgnoreCase(value.getName());
    }
}
