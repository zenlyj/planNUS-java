package com.orbital.planNUS.task;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbital.planNUS.config.JwtAuthFilter;
import com.orbital.planNUS.config.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.orbital.planNUS.task.TaskFixture.taskFixture;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = TaskController.class,
    excludeFilters = {
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class),
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtService.class)
    })
@AutoConfigureMockMvc(addFilters = false)
public class TaskControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @MockitoBean private TaskService taskService;

  private final Long STUDENT_ID = 1L;
  private final Long BAD_STUDENT_ID = -1L;
  private final Long ID = 1L;
  private final Long BAD_ID = -1L;
  private final String ACADEMIC_YEAR = "2025-2026";

  @Test
  void shouldReturnTasks() throws Exception {
    final var task = taskFixture(STUDENT_ID);
    when(taskService.getTasksByStudentId(STUDENT_ID)).thenReturn(List.of(task));

    mockMvc
        .perform(get("/api/tasks", STUDENT_ID).param("studentId", STUDENT_ID.toString()))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(List.of(task))));
  }

  @Test
  void shouldNotReturnTasks() throws Exception {
    final var task = taskFixture(BAD_STUDENT_ID);
    when(taskService.getTasksByStudentId(BAD_STUDENT_ID)).thenReturn(List.of(task));

    mockMvc
        .perform(get("/api/tasks", BAD_STUDENT_ID).param("studentId", BAD_STUDENT_ID.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldGetTaskWorkloads() throws Exception {
    final var taskWorkloadViewResponse = new TaskWorkloadViewResponse("cs101", 8);
    when(taskService.getTaskWorkloadsByStudentId(STUDENT_ID, ACADEMIC_YEAR))
        .thenReturn(List.of(taskWorkloadViewResponse));

    mockMvc
        .perform(
            get("/api/tasks/workload")
                .param("studentId", STUDENT_ID.toString())
                .param("academicYear", ACADEMIC_YEAR))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(List.of(taskWorkloadViewResponse))));
  }

  @Test
  void shouldNotGetTaskWorkloads() throws Exception {
    final var taskWorkloadViewResponse = new TaskWorkloadViewResponse("cs101", 8);
    when(taskService.getTaskWorkloadsByStudentId(BAD_STUDENT_ID, ACADEMIC_YEAR))
        .thenReturn(List.of(taskWorkloadViewResponse));

    mockMvc
        .perform(
            get("/api/tasks/workload")
                .param("studentId", BAD_STUDENT_ID.toString())
                .param("academicYear", ACADEMIC_YEAR))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldCreateTask() throws Exception {
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
    when(taskService.addTask(taskCreateRequest)).thenReturn(task);

    mockMvc
        .perform(
            post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(taskCreateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(task)));
  }

  @Test
  void shouldNotCreateTask() throws Exception {
    final var task = taskFixture(STUDENT_ID);
    final var taskCreateRequest =
        new TaskCreateRequest(
            task.getStudentId(),
            task.getName(),
            null,
            task.getEndTime(),
            task.getDate(),
            task.getModule(),
            task.getDescription());
    when(taskService.addTask(taskCreateRequest)).thenReturn(task);

    mockMvc
        .perform(
            post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(taskCreateRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldImportTasks() throws Exception {
    final var task = taskFixture(STUDENT_ID);
    final var taskImportRequest =
        new TaskImportRequest(STUDENT_ID, "link", ACADEMIC_YEAR, 1, 13, LocalDate.of(2025, 12, 12));
    when(taskService.importTasks(taskImportRequest)).thenReturn(List.of(task));

    mockMvc
        .perform(
            post("/api/tasks/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(taskImportRequest)))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(List.of(task))));
  }

  @Test
  void shouldNotImportTasks() throws Exception {
    final var task = taskFixture(STUDENT_ID);
    final var taskImportRequest =
        new TaskImportRequest(STUDENT_ID, null, ACADEMIC_YEAR, 1, 13, LocalDate.of(2025, 12, 12));
    when(taskService.importTasks(taskImportRequest)).thenReturn(List.of(task));

    mockMvc
        .perform(
            post("/api/tasks/import")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(taskImportRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldDeleteTask() throws Exception {
    mockMvc
        .perform(delete("/api/tasks/{id}", ID))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));
  }

  @Test
  void shouldNotDeleteTask() throws Exception {
    mockMvc.perform(delete("/api/tasks/{id}", BAD_ID)).andExpect(status().isBadRequest());
  }

  @Test
  void shouldUpdateTask() throws Exception {
    final var task = taskFixture(STUDENT_ID);
    final var taskUpdateRequest =
        new TaskUpdateRequest(
            task.getName(),
            task.getStartTime(),
            task.getEndTime(),
            task.getModule(),
            task.getDescription(),
            task.getIsCompleted());
    when(taskService.updateTask(STUDENT_ID, taskUpdateRequest)).thenReturn(task);

    mockMvc
        .perform(
            put("/api/tasks/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(taskUpdateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(task)));
  }

  @Test
  void shouldNotUpdateTask() throws Exception {
    final var task = taskFixture(STUDENT_ID);
    final var taskUpdateRequest =
        new TaskUpdateRequest(
            null,
            task.getStartTime(),
            task.getEndTime(),
            task.getModule(),
            task.getDescription(),
            task.getIsCompleted());
    when(taskService.updateTask(STUDENT_ID, taskUpdateRequest)).thenReturn(task);

    mockMvc
        .perform(
            put("/api/tasks/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(taskUpdateRequest)))
        .andExpect(status().isBadRequest());
  }
}
