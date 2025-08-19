package com.orbital.planNUS.repository;

import com.orbital.planNUS.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByStudentId(Long studentId);
  List<Task> findByStudentIdAndDate(Long studentId, LocalDate date);
}
