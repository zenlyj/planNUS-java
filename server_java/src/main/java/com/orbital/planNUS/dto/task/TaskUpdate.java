package com.orbital.planNUS.dto.task;

import java.time.LocalTime;

public record TaskUpdate(
    String name,
    String module,
    String description,
    LocalTime startTime,
    LocalTime endTime,
    Boolean isCompleted) {}
