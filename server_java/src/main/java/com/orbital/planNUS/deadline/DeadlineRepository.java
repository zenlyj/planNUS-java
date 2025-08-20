package com.orbital.planNUS.deadline;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DeadlineRepository extends JpaRepository<Deadline, Long> {
  List<Deadline> findByStudentId(Long studentId);

  List<Deadline> findByStudentIdAndDate(Long studentId, LocalDate date);
}
