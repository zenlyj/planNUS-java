package com.orbital.planNUS.task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public ResponseEntity<List<Task>> getTasks(@RequestParam Long studentId) {
    return ResponseEntity.ok(taskService.getTasksByStudentId(studentId));
  }

  @GetMapping("workload")
  public ResponseEntity<List<TaskWorkloadViewResponse>> getTaskWorkloads(
      @RequestParam Long studentId, @RequestParam String academicYear) {
    return ResponseEntity.ok(taskService.getTaskWorkloadsByStudentId(studentId, academicYear));
  }

  @PostMapping
  public ResponseEntity<Task> createTask(@RequestBody TaskCreateRequest req) {
    return ResponseEntity.ok(taskService.addTask(req));
  }

  @PostMapping("import")
  public ResponseEntity<List<Task>> importTasks(@RequestBody TaskImportRequest req) {
    return ResponseEntity.ok(taskService.importTasks(req));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}")
  public ResponseEntity<Task> updateTask(
      @PathVariable Long id, @RequestBody TaskUpdateRequest req) {
    return ResponseEntity.ok(taskService.updateTask(id, req));
  }
}
