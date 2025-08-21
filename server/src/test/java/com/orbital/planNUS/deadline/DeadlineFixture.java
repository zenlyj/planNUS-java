package com.orbital.planNUS.deadline;

import java.time.LocalDate;

public class DeadlineFixture {
  public static Deadline deadlineFixture(Long studentId) {
    return Deadline.builder()
        .id(1L)
        .studentId(studentId)
        .date(LocalDate.of(2025, 12, 12))
        .name("lab 1")
        .module("cs101")
        .description("test")
        .build();
  }
}
