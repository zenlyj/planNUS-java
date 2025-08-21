package com.orbital.planNUS.diary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

import static com.orbital.planNUS.diary.DiaryFixture.diaryFixture;
import static com.orbital.planNUS.diary.DiaryFixture.diaryViewResponseFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class DiaryControllerTest {
  @Mock private DiaryService diaryService;

  @InjectMocks private DiaryController diaryController;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldGetStudentDiaries() {
    final var studentId = 1L;
    final var diaryViewResponse = diaryViewResponseFixture(studentId);
    when(diaryService.getDiariesByStudentId(studentId)).thenReturn(List.of(diaryViewResponse));

    final var response = diaryController.getDiaries(studentId);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(List.of(diaryViewResponse), response.getBody());

    verify(diaryService, times(1)).getDiariesByStudentId(studentId);
  }

  @Test
  void shouldGetStudentDiaryIfPresentElseCreate() {
    final var studentId = 1L;
    final var diaryViewResponse = diaryViewResponseFixture(studentId);
    final var date = LocalDate.of(2025, 12, 12);
    when(diaryService.getsertDiaryByStudentId(studentId, date)).thenReturn(diaryViewResponse);

    final var response = diaryController.getsertDiary(studentId, date);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(diaryViewResponse, response.getBody());

    verify(diaryService, times(1)).getsertDiaryByStudentId(studentId, date);
  }

  @Test
  void shouldCreateDiary() {
    final var studentId = 1L;
    final var diaryCreateRequest =
        new DiaryCreateRequest(studentId, LocalDate.of(2025, 12, 12), "test");
    final var diary = diaryFixture(studentId);
    when(diaryService.addDiary(diaryCreateRequest)).thenReturn(diary);

    final var response = diaryController.createDiary(diaryCreateRequest);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(diary, response.getBody());

    verify(diaryService, times(1)).addDiary(diaryCreateRequest);
  }

  @Test
  void shouldDeleteDiary() {
    final var id = 1L;

    final var response = diaryController.deleteDiary(id);

    assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
    assertNull(response.getBody());

    verify(diaryService, times(1)).deleteDiary(id);
  }

  @Test
  void shouldUpdateDiary() {
    final var id = 1L;
    final var diaryUpdateRequest = new DiaryUpdateRequest("test");
    final var diary = diaryFixture(1L);
    when(diaryService.updateDiary(id, diaryUpdateRequest)).thenReturn(diary);

    final var response = diaryController.updateDiary(id, diaryUpdateRequest);

    assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
    assertEquals(diary, response.getBody());

    verify(diaryService, times(1)).updateDiary(id, diaryUpdateRequest);
  }
}
