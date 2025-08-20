package com.orbital.planNUS.task;

import com.orbital.planNUS.validator.HasStartEnd;
import com.orbital.planNUS.validator.StartBeforeEnd;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

@StartBeforeEnd
public record TaskUpdateRequest(
    @NotBlank(message = "Name is required") String name,
    @NotNull(message = "Start time is required") LocalTime startTime,
    @NotNull(message = "End time is required") LocalTime endTime,
    String module,
    String description,
    Boolean isCompleted)
    implements HasStartEnd {}
