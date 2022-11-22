package ru.javawebinar.topjava.repository.jdbc;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

public class JdbcUtil {

    public static <T> void validate(T t, Validator validator) {
        Set<ConstraintViolation<T>> violations = validator.validate(t);
        if (violations.size() > 0) {
            throw new ConstraintViolationException(violations);
        }
    }
}
