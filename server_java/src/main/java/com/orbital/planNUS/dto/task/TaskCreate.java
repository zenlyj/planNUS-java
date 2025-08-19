package com.orbital.planNUS.dto.task;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskCreate(
    Long studentId,
    String name,
    String module,
    String description,
    LocalTime startTime,
    LocalTime endTime,
    LocalDate date) {}
