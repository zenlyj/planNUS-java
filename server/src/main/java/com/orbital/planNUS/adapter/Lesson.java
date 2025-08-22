package com.orbital.planNUS.adapter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Lesson {
  private String moduleCode;
  private String lessonType;
  private String lessonId;
  private int workloadHours;
  private String startTime;
  private String endTime;
  private List<Integer> weeks;
  private String day;

  public Lesson(String moduleCode, String lessonType, String lessonId) {
    this.moduleCode = moduleCode;
    this.lessonType = lessonType;
    this.lessonId = lessonId;
    this.workloadHours = 0;
  }

  public Lesson(Lesson lesson) {
    this.moduleCode = lesson.getModuleCode();
    this.lessonType = lesson.getLessonType();
    this.lessonId = lesson.getLessonId();
    this.workloadHours = lesson.getWorkloadHours();
    this.weeks = lesson.getWeeks();
  }
}
