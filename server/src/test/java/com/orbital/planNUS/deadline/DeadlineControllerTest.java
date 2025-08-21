package com.orbital.planNUS.deadline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

import static com.orbital.planNUS.deadline.DeadlineFixture.deadlineFixture;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DeadlineControllerTest {
  @Mock private DeadlineService deadlineService;

  @InjectMocks private DeadlineController deadlineController;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldReturnDeadlines() {
    final var studentId = 1L;
    final var deadline = deadlineFixture(studentId);
    when(deadlineService.getDeadlinesByStudentId(studentId)).thenReturn(List.of(deadline));

    final var response = deadlineController.getDeadlines(studentId);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(List.of(deadline), response.getBody());

    verify(deadlineService, times(1)).getDeadlinesByStudentId(studentId);
  }

  @Test
  void shouldCreateDeadline() {
    final var studentId = 1L;
    final var deadlineCreateRequest =
        new DeadlineCreateRequest(studentId, "lab 1", LocalDate.of(2025, 12, 12), "cs101", "test");
    final var deadline = deadlineFixture(studentId);
    when(deadlineService.addDeadline(deadlineCreateRequest)).thenReturn(deadline);

    final var response = deadlineController.createDeadline(deadlineCreateRequest);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(deadline, response.getBody());

    verify(deadlineService, times(1)).addDeadline(deadlineCreateRequest);
  }

  @Test
  void shouldDeleteDeadline() {
    final var id = 1L;

    final var response = deadlineController.deleteDeadline(id);

    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    assertNull(response.getBody());

    verify(deadlineService, times(1)).deleteDeadline(id);
  }

  @Test
  void shouldUpdateDeadline() {
    final var id = 1L;
    final var deadlineUpdateRequest =
        new DeadlineUpdateRequest("lab 1", LocalDate.of(2025, 12, 12), "cs101", "test");
    final var deadline = deadlineFixture(1L);
    when(deadlineService.updateDeadline(id, deadlineUpdateRequest)).thenReturn(deadline);

    final var response = deadlineController.updateDeadline(id, deadlineUpdateRequest);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(deadline, response.getBody());

    verify(deadlineService, times(1)).updateDeadline(id, deadlineUpdateRequest);
  }
}
