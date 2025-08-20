package com.orbital.planNUS.task;

import java.time.LocalDate;

public record TaskImportRequest(
    Long studentId,
    String link,
    String academicYear,
    Integer semesterNum,
    Integer numWeeks,
    LocalDate startDate) {}
