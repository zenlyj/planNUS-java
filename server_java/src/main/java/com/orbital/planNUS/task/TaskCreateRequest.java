package com.orbital.planNUS.task;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskCreateRequest(
    Long studentId,
    String name,
    String module,
    String description,
    LocalTime startTime,
    LocalTime endTime,
    LocalDate date) {}
