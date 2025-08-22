package com.orbital.planNUS.diary;

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

import static com.orbital.planNUS.diary.DiaryFixture.diaryFixture;
import static com.orbital.planNUS.diary.DiaryFixture.diaryViewResponseFixture;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
    controllers = DiaryController.class,
    excludeFilters = {
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthFilter.class),
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtService.class)
    })
@AutoConfigureMockMvc(addFilters = false)
public class DiaryControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @MockitoBean private DiaryService diaryService;

  private final Long STUDENT_ID = 1L;
  private final Long BAD_STUDENT_ID = -1L;
  private final Long ID = 1L;
  private final Long BAD_ID = -1L;

  @Test
  void shouldGetStudentDiaries() throws Exception {
    final var diaryViewResponse = diaryViewResponseFixture(STUDENT_ID);
    when(diaryService.getDiariesByStudentId(STUDENT_ID)).thenReturn(List.of(diaryViewResponse));

    mockMvc
        .perform(get("/api/diaries", STUDENT_ID).param("studentId", STUDENT_ID.toString()))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(List.of(diaryViewResponse))));
  }

  @Test
  void shouldNotGetStudentDiaries() throws Exception {
    final var diaryViewResponse = diaryViewResponseFixture(BAD_STUDENT_ID);
    when(diaryService.getDiariesByStudentId(BAD_STUDENT_ID)).thenReturn(List.of(diaryViewResponse));

    mockMvc
        .perform(get("/api/diaries", BAD_STUDENT_ID).param("studentId", BAD_STUDENT_ID.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldGetSertStudentDiary() throws Exception {
    final var diaryViewResponse = diaryViewResponseFixture(STUDENT_ID);
    final var date = LocalDate.of(2025, 12, 12);
    when(diaryService.getsertDiaryByStudentId(STUDENT_ID, date)).thenReturn(diaryViewResponse);

    mockMvc
        .perform(get("/api/diaries/{date}", date).param("studentId", STUDENT_ID.toString()))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(diaryViewResponse)));
  }

  @Test
  void shouldNotGetSertStudentDiary() throws Exception {
    final var diaryViewResponse = diaryViewResponseFixture(BAD_STUDENT_ID);
    final var date = LocalDate.of(2025, 12, 12);
    when(diaryService.getsertDiaryByStudentId(BAD_STUDENT_ID, date)).thenReturn(diaryViewResponse);

    mockMvc
        .perform(get("/api/diaries/{date}", date).param("studentId", BAD_STUDENT_ID.toString()))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldCreateDiary() throws Exception {
    final var diaryCreateRequest =
        new DiaryCreateRequest(STUDENT_ID, LocalDate.of(2025, 12, 12), "test");
    final var diary = diaryFixture(STUDENT_ID);
    when(diaryService.addDiary(diaryCreateRequest)).thenReturn(diary);

    mockMvc
        .perform(
            post("/api/diaries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(diaryCreateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(diary)));
  }

  @Test
  void shouldNotCreateDiary() throws Exception {
    final var diaryCreateRequest = new DiaryCreateRequest(null, LocalDate.of(2025, 12, 12), "test");
    final var diary = diaryFixture(STUDENT_ID);
    when(diaryService.addDiary(diaryCreateRequest)).thenReturn(diary);

    mockMvc
        .perform(
            post("/api/diaries")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(diaryCreateRequest)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldDeleteDiary() throws Exception {
    mockMvc
        .perform(delete("/api/diaries/{id}", ID))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));
  }

  @Test
  void shouldNotDeleteDiary() throws Exception {
    mockMvc.perform(delete("/api/diaries/{id}", BAD_ID)).andExpect(status().isBadRequest());
  }

  @Test
  void shouldUpdateDiary() throws Exception {
    final var diaryUpdateRequest = new DiaryUpdateRequest("test");
    final var diary = diaryFixture(STUDENT_ID);
    when(diaryService.updateDiary(ID, diaryUpdateRequest)).thenReturn(diary);

    mockMvc
        .perform(
            patch("/api/diaries/{id}", ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(diaryUpdateRequest)))
        .andExpect(status().isOk())
        .andExpect(content().json(mapper.writeValueAsString(diary)));
  }

  @Test
  void shouldNotUpdateDiary() throws Exception {
    final var diaryUpdateRequest = new DiaryUpdateRequest("test");
    final var diary = diaryFixture(STUDENT_ID);
    when(diaryService.updateDiary(BAD_ID, diaryUpdateRequest)).thenReturn(diary);

    mockMvc
        .perform(
            patch("/api/diaries/{id}", BAD_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(diaryUpdateRequest)))
        .andExpect(status().isBadRequest());
  }
}
