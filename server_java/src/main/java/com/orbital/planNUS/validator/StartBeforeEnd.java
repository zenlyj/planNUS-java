package com.orbital.planNUS.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Documented
public @interface StartBeforeEnd {
  String message() default "Start time must be before end time";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
