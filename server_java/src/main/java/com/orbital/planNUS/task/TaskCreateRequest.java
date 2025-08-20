package com.orbital.planNUS.task;

import com.orbital.planNUS.validator.HasStartEnd;
import com.orbital.planNUS.validator.StartBeforeEnd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

@StartBeforeEnd()
public record TaskCreateRequest(
    @NotNull(message = "Student ID is required") Long studentId,
    @NotBlank(message = "Name is required") String name,
    @NotNull(message = "Start time is required") LocalTime startTime,
    @NotNull(message = "End time is required") LocalTime endTime,
    @NotNull(message = "Date is required") LocalDate date,
    String module,
    String description)
    implements HasStartEnd {}
