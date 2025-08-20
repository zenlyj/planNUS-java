package com.orbital.planNUS.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskImportRequest(
    @NotNull(message = "Student ID is required") Long studentId,
    @NotBlank(message = "Link is required") String link,
    @NotBlank(message = "Academic year is required") String academicYear,
    @NotNull(message = "Semester is required") Integer semesterNum,
    @NotNull(message = "Number of weeks is required") Integer numWeeks,
    @NotNull(message = "Start date is required") LocalDate startDate) {}
