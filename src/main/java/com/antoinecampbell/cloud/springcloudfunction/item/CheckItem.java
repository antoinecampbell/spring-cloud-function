package com.antoinecampbell.cloud.springcloudfunction.item;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckItemValidator.class)
@Documented
public @interface CheckItem {

    String message() default "Item has bad name";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
