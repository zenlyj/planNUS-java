package com.orbital.planNUS.diary;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DiaryCreateRequest(
    @NotNull(message = "Student ID is required") Long studentId,
    @NotNull(message = "Date is required") LocalDate date,
    String note) {}
