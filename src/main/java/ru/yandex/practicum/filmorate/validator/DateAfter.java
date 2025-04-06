package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = DateAfterValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Repeatable(DateAfter.List.class)
public @interface DateAfter {

    String message() default "{ru.yandex.practicum.filmorate.validator.DateAfter.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int referenceDay();

    int referenceMonth();

    int referenceYear();

    boolean inclusive() default true;

    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
    @Retention(RUNTIME)
    @interface List {

        DateAfter[] value();
    }
}
