package com.orbital.planNUS.deadline;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orbital.planNUS.config.JwtAuthFilter;
import com.orbital.planNUS.config.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static com.orbital.planNUS.deadline.DeadlineFixture.deadlineFixture;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = DeadlineController.class,
    excludeFilters = {
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class),
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtService.class)
    })
@AutoConfigureMockMvc(addFilters = false)
public class DeadlineControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @MockitoBean private DeadlineService deadlineService;

  private final Long STUDENT_ID = 1L;
  private final Long BAD_STUDENT_ID = -1L;
  private final Long ID = 1L;
  private final Long BAD_ID = -1L;

  @Test
  void shouldReturnDeadlines() throws Exception {
    final var deadline = deadlineFixture(STUDENT_ID);
    when(deadlineService.getDeadlinesByStudentId(STUDENT_ID)).thenReturn(List.of(deadline));

    mockMvc
        .perform(get("/api/deadlines", STUDENT_ID).param("studentId", STUDENT_ID.toString()))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(List.of(deadline))));
  }

  @Test
  void shouldNotReturnDeadlines() throws Exception {
    final var deadline = deadlineFixture(BAD_STUDENT_ID);
    when(deadlineService.getDeadlinesByStudentId(BAD_STUDENT_ID)).thenReturn(List.of(deadline));

    mockMvc
        .perform(
            get("/api/deadlines", BAD_STUDENT_ID).param("studentId", BAD_STUDENT_ID.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldCreateDeadline() throws Exception {
    final var deadlineCreateRequest =
        new DeadlineCreateRequest(STUDENT_ID, "lab 1", LocalDate.of(2025, 12, 12), "cs101", "test");
    final var deadline = deadlineFixture(STUDENT_ID);
    when(deadlineService.addDeadline(deadlineCreateRequest)).thenReturn(deadline);

    mockMvc
        .perform(
            post("/api/deadlines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(deadlineCreateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(deadline)));
  }

  @Test
  void shouldNotCreateDeadline() throws Exception {
    final var deadlineCreateRequest =
        new DeadlineCreateRequest(null, "lab 1", LocalDate.of(2025, 12, 12), "cs101", "test");
    final var deadline = deadlineFixture(STUDENT_ID);
    when(deadlineService.addDeadline(deadlineCreateRequest)).thenReturn(deadline);

    mockMvc
        .perform(
            post("/api/deadlines")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(deadlineCreateRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldDeleteDeadline() throws Exception {
    mockMvc
        .perform(delete("/api/deadlines/{id}", ID))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));
  }

  @Test
  void shouldNotDeleteDeadline() throws Exception {
    mockMvc.perform(delete("/api/deadlines/{id}", BAD_ID)).andExpect(status().isBadRequest());
  }

  @Test
  void shouldUpdateDeadline() throws Exception {
    final var deadlineUpdateRequest =
        new DeadlineUpdateRequest("lab 1", LocalDate.of(2025, 12, 12), "cs101", "test");
    final var deadline = deadlineFixture(STUDENT_ID);
    when(deadlineService.updateDeadline(ID, deadlineUpdateRequest)).thenReturn(deadline);

    mockMvc
        .perform(
            put("/api/deadlines/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(deadlineUpdateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(deadline)));
  }

  @Test
  void shouldNotUpdateDeadline() throws Exception {
    final var deadlineUpdateRequest =
        new DeadlineUpdateRequest(null, LocalDate.of(2025, 12, 12), "cs101", "test");
    final var deadline = deadlineFixture(STUDENT_ID);
    when(deadlineService.updateDeadline(ID, deadlineUpdateRequest)).thenReturn(deadline);

    mockMvc
        .perform(
            put("/api/deadlines/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(deadlineUpdateRequest)))
        .andExpect(status().isBadRequest());
  }
}
