package com.orbital.planNUS.deadline;

import java.time.LocalDate;

public record DeadlineUpdateRequest(String name, String module, LocalDate date, String description) {}
