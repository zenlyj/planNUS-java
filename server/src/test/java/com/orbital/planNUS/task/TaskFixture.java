package com.orbital.planNUS.task;

import java.time.LocalDate;
import java.time.LocalTime;

public class TaskFixture {
  public static Task taskFixture(Long studentId) {
    return Task.builder()
        .id(1L)
        .studentId(studentId)
        .startTime(LocalTime.of(12, 12))
        .endTime(LocalTime.of(13, 13))
        .date(LocalDate.of(2025, 12, 12))
        .isCompleted(false)
        .name("lab 1")
        .module("cs101")
        .description("")
        .build();
  }
}
