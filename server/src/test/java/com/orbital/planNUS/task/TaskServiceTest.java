package com.orbital.planNUS.task;

import com.orbital.planNUS.adapter.ImportService;
import com.orbital.planNUS.adapter.Lesson;
import com.orbital.planNUS.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.orbital.planNUS.task.TaskFixture.taskFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class TaskServiceTest {
  @Mock private TaskRepository taskRepository;

  @Mock private ImportService importService;

  @InjectMocks private TaskService taskService;

  private final Long STUDENT_ID = 1L;
  private final Long ID = 1L;
  private final String ACADEMIC_YEAR = "2025-2026";

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldGetTasksByStudentId() {
    final var task = taskFixture(STUDENT_ID);
    when(taskRepository.findByStudentId(STUDENT_ID)).thenReturn(List.of(task));

    final var tasks = taskService.getTasksByStudentId(STUDENT_ID);

    assertEquals(tasks, List.of(task));

    verify(taskRepository, times(1)).findByStudentId(STUDENT_ID);
  }

  @Test
  void shouldCreateNewTask() {
    final var task = taskFixture(STUDENT_ID);
    final var taskCreateRequest =
        new TaskCreateRequest(
            task.getStudentId(),
            task.getName(),
            task.getStartTime(),
            task.getEndTime(),
            task.getDate(),
            task.getModule(),
            task.getDescription());

    final var createdTask = taskService.addTask(taskCreateRequest);

    assertEquals(taskCreateRequest.studentId(), createdTask.getStudentId());
    assertEquals(taskCreateRequest.name(), createdTask.getName());
    assertEquals(taskCreateRequest.startTime(), createdTask.getStartTime());
    assertEquals(taskCreateRequest.endTime(), createdTask.getEndTime());
    assertEquals(taskCreateRequest.date(), createdTask.getDate());
    assertEquals(taskCreateRequest.module(), createdTask.getModule());
    assertEquals(taskCreateRequest.description(), createdTask.getDescription());

    verify(taskRepository, times(1)).save(createdTask);
  }

  @Test
  void shouldDeleteTask() {
    taskService.deleteTask(ID);

    verify(taskRepository, times(1)).deleteById(ID);
  }

  @Test
  void shouldUpdateTaskIfFound() {
    final var taskUpdateRequest =
        new TaskUpdateRequest(
            "lab 1", LocalTime.of(12, 12), LocalTime.of(13, 13), "cs101", "new", true);
    final var oldTask =
        Task.builder()
            .id(ID)
            .studentId(STUDENT_ID)
            .startTime(LocalTime.of(12, 12))
            .endTime(LocalTime.of(14, 14))
            .date(LocalDate.of(2025, 12, 12))
            .isCompleted(false)
            .name("old lab 1")
            .module("old cs101")
            .description("old")
            .build();
    when(taskRepository.findById(ID)).thenReturn(Optional.of(oldTask));

    final var task = taskService.updateTask(ID, taskUpdateRequest);

    assertEquals(taskUpdateRequest.name(), task.getName());
    assertEquals(taskUpdateRequest.startTime(), task.getStartTime());
    assertEquals(taskUpdateRequest.endTime(), task.getEndTime());
    assertEquals(taskUpdateRequest.module(), task.getModule());
    assertEquals(taskUpdateRequest.description(), task.getDescription());
    assertEquals(taskUpdateRequest.isCompleted(), task.getIsCompleted());

    verify(taskRepository, times(1)).save(task);
  }

  @Test
  void shouldNotUpdateTaskIfNotFound() {
    final var taskUpdateRequest =
        new TaskUpdateRequest(
            "lab 1", LocalTime.of(12, 12), LocalTime.of(13, 13), "cs101", "new", true);
    when(taskRepository.findById(ID)).thenThrow(new AppException("id", "Task not found"));

    assertThrows(AppException.class, () -> taskService.updateTask(ID, taskUpdateRequest));

    verify(taskRepository, never()).save(any());
  }

  @Test
  void shouldGetTasksByStudentIdAndDate() {
    final var task = taskFixture(STUDENT_ID);
    when(taskRepository.findByStudentIdAndDate(STUDENT_ID, task.getDate()))
        .thenReturn(List.of(task));

    final var tasks =
        taskService.getTasksByStudentIdAndDate(STUDENT_ID, LocalDate.of(2025, 12, 12));

    assertEquals(List.of(task), tasks);

    verify(taskRepository, times(1)).findByStudentIdAndDate(STUDENT_ID, task.getDate());
  }

  @Test
  void shouldImportTasks() {
    final List<Integer> weeks = List.of(1);
    final var taskImportRequest =
        new TaskImportRequest(
            STUDENT_ID, "link", ACADEMIC_YEAR, 1, weeks.size(), LocalDate.of(2025, 12, 12));
    final var lesson = new Lesson("cs101", "lecture", "05", 8, "1200", "1300", weeks, "Monday");
    when(importService.importLessons(
            taskImportRequest.link(),
            taskImportRequest.academicYear(),
            taskImportRequest.semesterNum()))
        .thenReturn(List.of(lesson));

    final var tasks = taskService.importTasks(taskImportRequest);
    assertEquals(1, weeks.size());
    final var expected = tasks.getFirst();

    assertEquals(expected.getStudentId(), taskImportRequest.studentId());
    assertEquals(
        expected.getName(), "%s %s".formatted(lesson.getModuleCode(), lesson.getLessonType()));
    assertEquals(expected.getModule(), lesson.getModuleCode());
    assertEquals("", expected.getDescription());
    assertEquals(
        expected.getStartTime(),
        LocalTime.of(
            Integer.parseInt(lesson.getStartTime().substring(0, 2)),
            Integer.parseInt(lesson.getStartTime().substring(2, 4))));
    assertEquals(
        expected.getEndTime(),
        LocalTime.of(
            Integer.parseInt(lesson.getEndTime().substring(0, 2)),
            Integer.parseInt(lesson.getEndTime().substring(2, 4))));
    assertEquals(expected.getDate(), LocalDate.of(2025, 12, 12));
    assertEquals(false, expected.getIsCompleted());

    verify(taskRepository, times(1)).saveAll(tasks);
  }

  @Test
  void shouldGetTaskWorkloadsByStudentId() {
    final var modules = List.of("cs101", "cs201", "cs301");
    final var workloadInHours = 8;
    when(taskRepository.findDistinctModulesByStudentId(STUDENT_ID)).thenReturn(modules);
    when(importService.getModuleWorkloadInHours(anyString(), eq(ACADEMIC_YEAR)))
        .thenReturn(workloadInHours);

    final var taskWorkloadViewResponses =
        taskService.getTaskWorkloadsByStudentId(STUDENT_ID, ACADEMIC_YEAR);

    assertEquals(
        taskWorkloadViewResponses,
        modules.stream()
            .map(module -> new TaskWorkloadViewResponse(module, workloadInHours))
            .toList());

    verify(taskRepository, times(1)).findDistinctModulesByStudentId(STUDENT_ID);
  }
}
