package com.orbital.planNUS.task;

import com.orbital.planNUS.adapter.SemesterCalendar;
import com.orbital.planNUS.adapter.Lesson;
import com.orbital.planNUS.adapter.ImportService;
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

  public Task addTask(TaskCreateRequest req) {
    var newTask =
        Task.builder()
            .studentId(req.studentId())
            .name(req.name())
            .module(req.module())
            .description(req.description())
            .startTime(req.startTime())
            .endTime(req.endTime())
            .date(req.date())
            .isCompleted(false)
            .build();

    taskRepository.save(newTask);
    return newTask;
  }

  public void deleteTask(Long id) {
    taskRepository.deleteById(id);
  }

  public Task updateTask(Long id, TaskUpdateRequest req) {
    var updatedTask =
        taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));

    updatedTask.setName(req.name());
    updatedTask.setModule(req.module());
    updatedTask.setDescription(req.description());
    updatedTask.setStartTime(req.startTime());
    updatedTask.setEndTime(req.endTime());
    updatedTask.setIsCompleted(req.isCompleted());

    taskRepository.save(updatedTask);
    return updatedTask;
  }

  public List<Task> getTasksByStudentIdAndDate(Long studentId, LocalDate date) {
    return taskRepository.findByStudentIdAndDate(studentId, date);
  }

  public List<Task> importTasks(TaskImportRequest req) {
    var lessons = importService.importLessons(req.link(), req.academicYear(), req.semesterNum());
    var newTasks =
        lessons.stream()
            .map(
                lesson ->
                    mapLessonToTasks(req.studentId(), lesson, req.numWeeks(), req.startDate()))
            .flatMap(Collection::stream)
            .toList();

    taskRepository.saveAll(newTasks);
    return newTasks;
  }

  public List<TaskWorkloadViewResponse> getTaskWorkloadsByStudentId(Long studentId, String academicYear) {
    List<String> modules = taskRepository.findDistinctModulesByStudentId(studentId);

    return modules.stream()
        .map(
            module ->
                new TaskWorkloadViewResponse(
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
