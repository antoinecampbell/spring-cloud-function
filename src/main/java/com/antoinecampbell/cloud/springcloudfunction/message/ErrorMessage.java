package com.antoinecampbell.cloud.springcloudfunction.message;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
public class ErrorMessage<T> {

    private String message;
    private List<Error> errors;

    public ErrorMessage(String message) {
        this.message = message;
    }

    public ErrorMessage(String message, Set<ConstraintViolation<T>> errors) {
        this(message);
        setErrors(errors);
    }

    private void setErrors(Set<ConstraintViolation<T>> errors) {
        this.errors = new ArrayList<>();
        errors.forEach(constraintViolation -> {
            String path = constraintViolation.getPropertyPath().toString();
            this.errors.add(new Error(path, constraintViolation.getMessage()));
        });
    }

    @AllArgsConstructor
    @Getter
    private static class Error {
        private final String field;
        private final String message;
    }
}
