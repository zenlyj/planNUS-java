package com.orbital.planNUS.task;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public ResponseEntity<List<Task>> getTasks(@RequestParam @NotNull @Positive Long studentId) {
    return ResponseEntity.ok(taskService.getTasksByStudentId(studentId));
  }

  @GetMapping("workload")
  public ResponseEntity<List<TaskWorkloadViewResponse>> getTaskWorkloads(
      @RequestParam @NotNull @Positive Long studentId,
      @RequestParam @NotBlank String academicYear) {
    return ResponseEntity.ok(taskService.getTaskWorkloadsByStudentId(studentId, academicYear));
  }

  @PostMapping
  public ResponseEntity<Task> createTask(@RequestBody @Valid TaskCreateRequest req) {
    return ResponseEntity.ok(taskService.addTask(req));
  }

  @PostMapping("import")
  public ResponseEntity<List<Task>> importTasks(@RequestBody @Valid TaskImportRequest req) {
    return ResponseEntity.ok(taskService.importTasks(req));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable @Positive Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}")
  public ResponseEntity<Task> updateTask(
      @PathVariable @Positive Long id, @RequestBody @Valid TaskUpdateRequest req) {
    return ResponseEntity.ok(taskService.updateTask(id, req));
  }
}
