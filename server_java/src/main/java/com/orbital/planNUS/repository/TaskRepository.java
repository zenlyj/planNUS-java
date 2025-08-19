package com.orbital.planNUS.repository;

import com.orbital.planNUS.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByStudentId(Long studentId);

  List<Task> findByStudentIdAndDate(Long studentId, LocalDate date);

  @Query("select distinct t.module from Task t where t.studentId = :studentId")
  List<String> findDistinctModulesByStudentId(@Param("studentId") Long studentId);
}
