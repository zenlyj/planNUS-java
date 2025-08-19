package com.orbital.planNUS.dto.task;

import java.time.LocalDate;

public record TaskImport(
    Long studentId,
    String link,
    String academicYear,
    Integer semesterNum,
    Integer numWeeks,
    LocalDate startDate) {}
