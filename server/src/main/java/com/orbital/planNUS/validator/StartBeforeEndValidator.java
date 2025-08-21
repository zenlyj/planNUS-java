package com.orbital.planNUS.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalTime;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, HasStartEnd> {

  @Override
  public boolean isValid(HasStartEnd value, ConstraintValidatorContext context) {
    if (value == null) return true; // let @NotNull handle nulls

    LocalTime start = value.startTime();
    LocalTime end = value.endTime();

    if (start == null || end == null) return true; // let @NotNull handle this

    if (!start.isBefore(end)) {
      context.disableDefaultConstraintViolation();
      context
          .buildConstraintViolationWithTemplate("Start time must be before end time")
          .addPropertyNode("endTime") // attach error to endTime field
          .addConstraintViolation();
      return false;
    }

    return true;
  }
}
