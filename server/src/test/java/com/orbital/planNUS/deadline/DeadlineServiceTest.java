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

  private final Long STUDENT_ID = 1L;
  private final Long ID = 1L;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldGetDeadlinesByStudentId() {
    final var deadline = deadlineFixture(STUDENT_ID);
    when(deadlineRepository.findByStudentId(STUDENT_ID)).thenReturn(List.of(deadline));

    final var studentDeadlines = deadlineService.getDeadlinesByStudentId(STUDENT_ID);

    assertEquals(List.of(deadline), studentDeadlines);

    verify(deadlineRepository, times(1)).findByStudentId(STUDENT_ID);
  }

  @Test
  void shouldCreateNewDeadline() {
    final var deadlineCreateRequest =
        new DeadlineCreateRequest(STUDENT_ID, "lab 1", LocalDate.of(2025, 12, 12), "cs101", "test");

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
    deadlineService.deleteDeadline(ID);

    verify(deadlineRepository, times(1)).deleteById(ID);
  }

  @Test
  void shouldUpdateDeadlineIfFound() {
    final var deadlineUpdateRequest =
        new DeadlineUpdateRequest("lab 1", LocalDate.of(2025, 12, 12), "cs101", "new");
    final var oldDeadline =
        Deadline.builder()
            .id(ID)
            .studentId(STUDENT_ID)
            .date(LocalDate.of(2025, 12, 10))
            .name("old lab 1")
            .module("old cs101")
            .description("old")
            .build();
    when(deadlineRepository.findById(ID)).thenReturn(Optional.of(oldDeadline));

    final var deadline = deadlineService.updateDeadline(ID, deadlineUpdateRequest);

    assertEquals(deadlineUpdateRequest.name(), deadline.getName());
    assertEquals(deadlineUpdateRequest.module(), deadline.getModule());
    assertEquals(deadlineUpdateRequest.date(), deadline.getDate());
    assertEquals(deadlineUpdateRequest.description(), deadline.getDescription());

    verify(deadlineRepository, times(1)).save(deadline);
  }

  @Test
  void shouldNotUpdateDeadlineIfNotFound() {
    final var deadlineUpdateRequest =
        new DeadlineUpdateRequest("lab 1", LocalDate.of(2025, 12, 12), "cs101", "new");
    when(deadlineRepository.findById(ID)).thenThrow(new AppException("id", "Deadline not found"));

    assertThrows(
        AppException.class, () -> deadlineService.updateDeadline(ID, deadlineUpdateRequest));

    verify(deadlineRepository, never()).save(any());
  }

  @Test
  void shouldGetDeadlinesByStudentIdAndDate() {
    final var deadline = deadlineFixture(STUDENT_ID);
    when(deadlineRepository.findByStudentIdAndDate(STUDENT_ID, deadline.getDate()))
        .thenReturn(List.of(deadline));

    final var deadlines =
        deadlineService.getDeadlinesByStudentIdAndDate(STUDENT_ID, deadline.getDate());

    assertEquals(List.of(deadline), deadlines);

    verify(deadlineRepository, times(1)).findByStudentIdAndDate(STUDENT_ID, deadline.getDate());
  }
}
