package com.orbital.planNUS.diary;

import com.orbital.planNUS.deadline.DeadlineService;
import com.orbital.planNUS.exception.AppException;
import com.orbital.planNUS.task.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.orbital.planNUS.diary.DiaryFixture.diaryFixture;
import static com.orbital.planNUS.diary.DiaryFixture.diaryViewResponseFixture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DiaryServiceTest {
  @Mock private DiaryRepository diaryRepository;

  @Mock private TaskService taskService;

  @Mock private DeadlineService deadlineService;

  @InjectMocks private DiaryService diaryService;

  @BeforeEach
  void setup() {
    MockitoAnnotations.openMocks(this);
    when(taskService.getTasksByStudentIdAndDate(any(), any())).thenReturn(List.of());
    when(deadlineService.getDeadlinesByStudentIdAndDate(any(), any())).thenReturn(List.of());
  }

  @Test
  void shouldGetDiariesByStudentId() {
    final var studentId = 1L;
    final var diary = diaryFixture(studentId);
    when(diaryRepository.findByStudentId(studentId)).thenReturn(List.of(diary));

    final var diaryViewResponses = diaryService.getDiariesByStudentId(studentId);

    assertEquals(List.of(diaryViewResponseFixture(diary)), diaryViewResponses);

    verify(diaryRepository, times(1)).findByStudentId(studentId);
  }

  @Test
  void shouldGetExistingStudentDiary() {
    final var studentId = 1L;
    final var diary = diaryFixture(studentId);
    when(diaryRepository.findByStudentIdAndDate(studentId, diary.getDate()))
        .thenReturn(List.of(diary));

    final var diaryViewResponse = diaryService.getsertDiaryByStudentId(studentId, diary.getDate());

    assertEquals(diaryViewResponseFixture(diary), diaryViewResponse);

    verify(diaryRepository, times(1)).findByStudentIdAndDate(studentId, diary.getDate());
  }

  @Test
  void shouldCreateDiaryIfNotExist() {
    final var studentId = 1L;
    final var date = LocalDate.of(2025, 12, 12);
    when(diaryRepository.findByStudentIdAndDate(studentId, date)).thenReturn(List.of());

    final var diaryViewResponse = diaryService.getsertDiaryByStudentId(studentId, date);

    assertEquals(
        diaryViewResponseFixture(Diary.builder().studentId(studentId).date(date).note("").build()),
        diaryViewResponse);

    verify(diaryRepository, times(1)).findByStudentIdAndDate(studentId, date);
  }

  @Test
  void shouldCreateDiary() {
    final var diaryCreateRequest = new DiaryCreateRequest(1L, LocalDate.of(2025, 12, 12), "test");

    final var diary = diaryService.addDiary(diaryCreateRequest);

    assertEquals(diaryCreateRequest.studentId(), diary.getStudentId());
    assertEquals(diaryCreateRequest.date(), diary.getDate());
    assertEquals(diaryCreateRequest.note(), diary.getNote());

    verify(diaryRepository, times(1)).save(diary);
  }

  @Test
  void shouldDeleteDiary() {
    final var id = 1L;

    diaryService.deleteDiary(id);

    verify(diaryRepository, times(1)).deleteById(id);
  }

  @Test
  void shouldUpdateDiaryIfFound() {
    final var id = 1L;
    final var diaryUpdateRequest = new DiaryUpdateRequest("new");
    final var oldDiary =
        Diary.builder().id(id).studentId(1L).date(LocalDate.of(2025, 12, 12)).note("old").build();

    when(diaryRepository.findById(id)).thenReturn(Optional.of(oldDiary));

    final var diary = diaryService.updateDiary(id, diaryUpdateRequest);

    assertEquals(diaryUpdateRequest.note(), diary.getNote());

    verify(diaryRepository, times(1)).save(diary);
  }

  @Test
  void shouldNotUpdateDiaryIfNotFound() {
    final var id = 1L;
    final var diaryUpdateRequest = new DiaryUpdateRequest("new");
    when(diaryRepository.findById(id)).thenThrow(new AppException("id", "Diary not found"));

    assertThrows(AppException.class, () -> diaryService.updateDiary(id, diaryUpdateRequest));

    verify(diaryRepository, never()).save(any());
  }
}
