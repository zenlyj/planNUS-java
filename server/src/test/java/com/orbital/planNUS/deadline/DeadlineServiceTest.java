package com.orbital.planNUS.deadline;

import com.orbital.planNUS.exception.AppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.orbital.planNUS.deadline.DeadlineFixture.deadlineFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DeadlineServiceTest {
  @Mock private DeadlineRepository deadlineRepository;

  @InjectMocks private DeadlineService deadlineService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldGetDeadlinesByStudentId() {
    final var studentId = 1L;
    final var deadline = deadlineFixture(studentId);
    when(deadlineRepository.findByStudentId(studentId)).thenReturn(List.of(deadline));

    final var studentDeadlines = deadlineService.getDeadlinesByStudentId(studentId);

    assertEquals(List.of(deadline), studentDeadlines);

    verify(deadlineRepository, times(1)).findByStudentId(studentId);
  }

  @Test
  void shouldCreateNewDeadline() {
    final var deadlineCreateRequest =
        new DeadlineCreateRequest(1L, "lab 1", LocalDate.of(2025, 12, 12), "cs101", "test");

    final var deadline = deadlineService.addDeadline(deadlineCreateRequest);

    assertEquals(deadlineCreateRequest.studentId(), deadline.getStudentId());
    assertEquals(deadlineCreateRequest.name(), deadline.getName());
    assertEquals(deadlineCreateRequest.date(), deadline.getDate());
    assertEquals(deadlineCreateRequest.module(), deadline.getModule());
    assertEquals(deadlineCreateRequest.description(), deadline.getDescription());

    verify(deadlineRepository, times(1)).save(deadline);
  }

  @Test
  void shouldDeleteDeadline() {
    final var id = 1L;

    deadlineService.deleteDeadline(id);

    verify(deadlineRepository, times(1)).deleteById(id);
  }

  @Test
  void shouldUpdateDeadlineIfFound() {
    final var id = 1L;
    final var deadlineUpdateRequest =
        new DeadlineUpdateRequest("old lab 1", LocalDate.of(2025, 12, 12), "old cs101", "old");
    final var oldDeadline =
        Deadline.builder()
            .id(id)
            .studentId(1L)
            .date(LocalDate.of(2025, 12, 10))
            .name("lab 1")
            .module("cs101")
            .description("new")
            .build();
    when(deadlineRepository.findById(id)).thenReturn(Optional.of(oldDeadline));

    final var deadline = deadlineService.updateDeadline(id, deadlineUpdateRequest);

    assertEquals(deadlineUpdateRequest.name(), deadline.getName());
    assertEquals(deadlineUpdateRequest.module(), deadline.getModule());
    assertEquals(deadlineUpdateRequest.date(), deadline.getDate());
    assertEquals(deadlineUpdateRequest.description(), deadline.getDescription());

    verify(deadlineRepository, times(1)).save(deadline);
  }

  @Test
  void shouldNotUpdateDeadlineIfNotFound() {
    final var id = 1L;
    final var deadlineUpdateRequest =
        new DeadlineUpdateRequest("old lab 1", LocalDate.of(2025, 12, 12), "old cs101", "old");
    when(deadlineRepository.findById(id)).thenThrow(new AppException("id", "Deadline not found"));

    assertThrows(
        AppException.class, () -> deadlineService.updateDeadline(id, deadlineUpdateRequest));

    verify(deadlineRepository, never()).save(any());
  }

  @Test
  void shouldGetDeadlinesByStudentIdAndDate() {
    final var studentId = 1L;
    final var deadline = deadlineFixture(studentId);
    when(deadlineRepository.findByStudentIdAndDate(studentId, deadline.getDate()))
        .thenReturn(List.of(deadline));

    final var deadlines =
        deadlineService.getDeadlinesByStudentIdAndDate(studentId, deadline.getDate());

    assertEquals(List.of(deadline), deadlines);

    verify(deadlineRepository, times(1)).findByStudentIdAndDate(studentId, deadline.getDate());
  }
}
