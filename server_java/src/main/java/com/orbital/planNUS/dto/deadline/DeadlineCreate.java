package com.orbital.planNUS.dto.deadline;

import java.time.LocalDate;

public record DeadlineCreate(
    Long studentId, String name, String module, LocalDate date, String description) {}
