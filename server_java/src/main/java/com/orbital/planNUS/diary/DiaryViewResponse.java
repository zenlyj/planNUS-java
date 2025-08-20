package com.orbital.planNUS.diary;

import com.orbital.planNUS.deadline.Deadline;
import com.orbital.planNUS.task.Task;

import java.time.LocalDate;
import java.util.List;

public record DiaryViewResponse(
    Long id,
    Long studentId,
    LocalDate date,
    String note,
    List<Task> tasks,
    List<Deadline> deadlines) {}
