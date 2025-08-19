package com.orbital.planNUS.dto.deadline;

import java.time.LocalDate;

public record DeadlineUpdate(String name, String module, LocalDate date, String description) {}
