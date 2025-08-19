package com.orbital.planNUS.service;

import com.orbital.planNUS.dto.task.TaskCreate;
import com.orbital.planNUS.dto.task.TaskUpdate;
import com.orbital.planNUS.model.Task;
import com.orbital.planNUS.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {
  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
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
}
