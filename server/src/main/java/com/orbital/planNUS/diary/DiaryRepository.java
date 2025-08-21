package com.orbital.planNUS.diary;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
  List<Diary> findByStudentId(Long studentId);

  List<Diary> findByStudentIdAndDate(Long studentId, LocalDate date);
}
