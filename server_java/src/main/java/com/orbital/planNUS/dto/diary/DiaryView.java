package com.orbital.planNUS.dto.diary;

import com.orbital.planNUS.model.Deadline;
import com.orbital.planNUS.model.Task;

import java.time.LocalDate;
import java.util.List;

public record DiaryView(
    Long id,
    Long studentId,
    LocalDate date,
    String note,
    List<Task> tasks,
    List<Deadline> deadlines) {}
