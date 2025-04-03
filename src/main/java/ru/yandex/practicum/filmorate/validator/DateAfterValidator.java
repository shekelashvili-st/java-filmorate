package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class DateAfterValidator implements ConstraintValidator<DateAfter, LocalDate> {

    private LocalDate referenceDate;
    private boolean inclusive;

    @Override
    public void initialize(DateAfter constraintAnnotation) {
        this.referenceDate = LocalDate.of(constraintAnnotation.referenceYear(),
                constraintAnnotation.referenceMonth(),
                constraintAnnotation.referenceDay());
        this.inclusive = constraintAnnotation.inclusive();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        // null values are valid
        if (value == null) {
            return true;
        }
        int comparisonResult = referenceDate.compareTo(value);
        return inclusive ? comparisonResult <= 0 : comparisonResult < 0;
    }

}
