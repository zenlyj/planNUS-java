package com.orbital.planNUS.deadline;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DeadlineUpdateRequest(
    @NotBlank(message = "Name is required") String name,
    @NotNull(message = "Date is required") LocalDate date,
    String module,
    String description) {}
