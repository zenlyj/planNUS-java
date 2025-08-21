package com.orbital.planNUS.deadline;

import com.orbital.planNUS.exception.AppException;
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

  public Deadline addDeadline(DeadlineCreateRequest req) {
    var newDeadline =
        Deadline.builder()
            .studentId(req.studentId())
            .name(req.name())
            .module(req.module())
            .date(req.date())
            .description(req.description())
            .build();

    deadlineRepository.save(newDeadline);
    return newDeadline;
  }

  public void deleteDeadline(Long id) {
    deadlineRepository.deleteById(id);
  }

  public Deadline updateDeadline(Long id, DeadlineUpdateRequest req) {
    var updatedDeadline =
        deadlineRepository.findById(id).orElseThrow(() -> new AppException("id", "Deadline not found"));

    updatedDeadline.setName(req.name());
    updatedDeadline.setModule(req.module());
    updatedDeadline.setDate(req.date());
    updatedDeadline.setDescription(req.description());

    deadlineRepository.save(updatedDeadline);
    return updatedDeadline;
  }

  public List<Deadline> getDeadlinesByStudentIdAndDate(Long studentId, LocalDate date) {
    return deadlineRepository.findByStudentIdAndDate(studentId, date);
  }
}
