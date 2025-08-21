package com.orbital.planNUS.diary;

import java.time.LocalDate;
import java.util.List;

public class DiaryFixture {
  public static DiaryViewResponse diaryViewResponseFixture(Long studentId) {
    return new DiaryViewResponse(
        1L, studentId, LocalDate.of(2025, 12, 12), "test", List.of(), List.of());
  }

  public static DiaryViewResponse diaryViewResponseFixture(Diary diary) {
    return new DiaryViewResponse(
        diary.getId(),
        diary.getStudentId(),
        diary.getDate(),
        diary.getNote(),
        List.of(),
        List.of());
  }

  public static Diary diaryFixture(Long studentId) {
    return new Diary(1L, studentId, LocalDate.of(2025, 12, 12), "test");
  }
}
