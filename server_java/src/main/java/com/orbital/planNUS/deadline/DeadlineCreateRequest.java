package com.orbital.planNUS.deadline;

import java.time.LocalDate;

public record DeadlineCreateRequest(
    Long studentId, String name, String module, LocalDate date, String description) {}
