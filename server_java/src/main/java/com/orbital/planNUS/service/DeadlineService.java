package com.orbital.planNUS.service;

import com.orbital.planNUS.dto.deadline.DeadlineCreate;
import com.orbital.planNUS.dto.deadline.DeadlineUpdate;
import com.orbital.planNUS.model.Deadline;
import com.orbital.planNUS.repository.DeadlineRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DeadlineService {
  private final DeadlineRepository deadlineRepository;

  public DeadlineService(DeadlineRepository deadlineRepository) {
    this.deadlineRepository = deadlineRepository;
  }

  public List<Deadline> getDeadlinesByStudentId(Long studentId) {
    return deadlineRepository.findByStudentId(studentId);
  }

  public Deadline addDeadline(DeadlineCreate deadline) {
    Deadline newDeadline =
        Deadline.builder()
            .studentId(deadline.studentId())
            .name(deadline.name())
            .module(deadline.module())
            .date(deadline.date())
            .description(deadline.description())
            .build();

    deadlineRepository.save(newDeadline);
    return newDeadline;
  }

  public void deleteDeadline(Long id) {
    deadlineRepository.deleteById(id);
  }

  public Deadline updateDeadline(Long id, DeadlineUpdate deadline) {
    Deadline updatedDeadline =
        deadlineRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Deadline not found"));

    updatedDeadline.setName(deadline.name());
    updatedDeadline.setModule(deadline.module());
    updatedDeadline.setDate(deadline.date());
    updatedDeadline.setDescription(deadline.description());

    deadlineRepository.save(updatedDeadline);
    return updatedDeadline;
  }

  public List<Deadline> getDeadlinesByStudentIdAndDate(Long studentId, LocalDate date) {
    return deadlineRepository.findByStudentIdAndDate(studentId, date);
  }
}
