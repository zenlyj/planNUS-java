package com.orbital.planNUS.task;

import java.time.LocalTime;

public record TaskUpdateRequest(
    String name,
    String module,
    String description,
    LocalTime startTime,
    LocalTime endTime,
    Boolean isCompleted) {}
