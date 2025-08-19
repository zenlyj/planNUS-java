package com.orbital.planNUS.service;

import com.orbital.planNUS.adapter.SemesterCalendar;
import com.orbital.planNUS.dto.lesson.Lesson;
import com.orbital.planNUS.dto.task.*;
import com.orbital.planNUS.model.Task;
import com.orbital.planNUS.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Service
public class TaskService {
  private final TaskRepository taskRepository;
  private final ImportService importService;

  public TaskService(TaskRepository taskRepository, ImportService importService) {
    this.taskRepository = taskRepository;
    this.importService = importService;
  }

  public List<Task> getTasksByStudentId(Long studentId) {
    return taskRepository.findByStudentId(studentId);
  }

  public Task addTask(TaskCreate task) {
    Task newTask =
        Task.builder()
            .studentId(task.studentId())
            .name(task.name())
            .module(task.module())
            .description(task.description())
            .startTime(task.startTime())
            .endTime(task.endTime())
            .date(task.date())
            .isCompleted(false)
            .build();

    taskRepository.save(newTask);
    return newTask;
  }

  public void deleteTask(Long id) {
    taskRepository.deleteById(id);
  }

  public Task updateTask(Long id, TaskUpdate task) {
    Task updatedTask =
        taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

    updatedTask.setName(task.name());
    updatedTask.setModule(task.module());
    updatedTask.setDescription(task.description());
    updatedTask.setStartTime(task.startTime());
    updatedTask.setEndTime(task.endTime());
    updatedTask.setIsCompleted(task.isCompleted());

    taskRepository.save(updatedTask);
    return updatedTask;
  }

  public List<Task> getTasksByStudentIdAndDate(Long studentId, LocalDate date) {
    return taskRepository.findByStudentIdAndDate(studentId, date);
  }

  public List<Task> importTasks(TaskImport tasks) {
    List<Lesson> lessons =
        importService.importLessons(tasks.link(), tasks.academicYear(), tasks.semesterNum());
    List<Task> newTasks =
        lessons.stream()
            .map(
                lesson ->
                    mapLessonToTasks(
                        tasks.studentId(), lesson, tasks.numWeeks(), tasks.startDate()))
            .flatMap(Collection::stream)
            .toList();

    taskRepository.saveAll(newTasks);
    return newTasks;
  }

  public List<TaskWorkloadView> getTaskWorkloadsByStudentId(Long studentId, String academicYear) {
    List<String> modules = taskRepository.findDistinctModulesByStudentId(studentId);

    return modules.stream()
        .map(
            module ->
                new TaskWorkloadView(
                    module, importService.getModuleWorkloadInHours(module, academicYear)))
        .toList();
  }

  private List<Task> mapLessonToTasks(
      Long studentId, Lesson lesson, int numWeeks, LocalDate startDate) {
    List<Integer> weeks = lesson.getWeeks();
    String day = lesson.getDay();
    List<List<LocalDate>> semesterDates = SemesterCalendar.weeklyDates(numWeeks, startDate);
    DateTimeFormatter dtFormatter = DateTimeFormatter.ofPattern("HHmm");

    return weeks.stream()
        .map(
            week -> {
              int dayIndex = DayOfWeek.valueOf(day.trim().toUpperCase(Locale.ROOT)).getValue() - 1;
              LocalDate date = semesterDates.get(week - 1).get(dayIndex);
              return Task.builder()
                  .studentId(studentId)
                  .name("%s %s".formatted(lesson.getModuleCode(), lesson.getLessonType()))
                  .module(lesson.getModuleCode())
                  .description("")
                  .startTime(LocalTime.parse(lesson.getStartTime(), dtFormatter))
                  .endTime(LocalTime.parse(lesson.getEndTime(), dtFormatter))
                  .date(date)
                  .isCompleted(false)
                  .build();
            })
        .toList();
  }
}
