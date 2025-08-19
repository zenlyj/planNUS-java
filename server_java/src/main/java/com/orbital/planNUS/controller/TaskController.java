package com.orbital.planNUS.controller;

import com.orbital.planNUS.dto.task.TaskCreate;
import com.orbital.planNUS.dto.task.TaskImport;
import com.orbital.planNUS.dto.task.TaskUpdate;
import com.orbital.planNUS.dto.task.TaskWorkloadView;
import com.orbital.planNUS.model.Task;
import com.orbital.planNUS.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<Task> getTasks(@RequestParam Long studentId) {
    return taskService.getTasksByStudentId(studentId);
  }

  @GetMapping("workload")
  public List<TaskWorkloadView> getTaskWorkloads(
      @RequestParam Long studentId, @RequestParam String academicYear) {
    return taskService.getTaskWorkloadsByStudentId(studentId, academicYear);
  }

  @PostMapping
  public Task createTask(@RequestBody TaskCreate task) {
    return taskService.addTask(task);
  }

  @PostMapping("import")
  public List<Task> importTasks(@RequestBody TaskImport tasks) {
    return taskService.importTasks(tasks);
  }

  @DeleteMapping("{id}")
  public void deleteTask(@PathVariable Long id) {
    taskService.deleteTask(id);
  }

  @PutMapping("{id}")
  public Task updateTask(@PathVariable Long id, @RequestBody TaskUpdate task) {
    return taskService.updateTask(id, task);
  }
}
