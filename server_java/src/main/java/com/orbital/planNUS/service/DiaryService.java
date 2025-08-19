package com.orbital.planNUS.service;

import com.orbital.planNUS.dto.diary.DiaryCreate;
import com.orbital.planNUS.dto.diary.DiaryUpdate;
import com.orbital.planNUS.dto.diary.DiaryView;
import com.orbital.planNUS.model.Diary;
import com.orbital.planNUS.repository.DiaryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DiaryService {
  private final DiaryRepository diaryRepository;
  private final TaskService taskService;
  private final DeadlineService deadlineService;

  public DiaryService(
      DiaryRepository diaryRepository, TaskService taskService, DeadlineService deadlineService) {
    this.diaryRepository = diaryRepository;
    this.taskService = taskService;
    this.deadlineService = deadlineService;
  }

  public List<DiaryView> getDiariesByStudentId(Long studentId) {
    var diaries = diaryRepository.findByStudentId(studentId);
    return diaries.stream().map(this::mapDiaryToDiaryView).toList();
  }

  public DiaryView getsertDiaryByStudentId(Long studentId, LocalDate date) {
    Optional<Diary> diary =
        diaryRepository.findByStudentIdAndDate(studentId, date).stream().findFirst();

    if (diary.isPresent()) {
      return mapDiaryToDiaryView(diary.get());
    }

    Diary newDiary = Diary.builder().studentId(studentId).date(date).note("").build();
    diaryRepository.save(newDiary);
    return mapDiaryToDiaryView(newDiary);
  }

  public Diary addDiary(DiaryCreate diary) {
    Diary newDiary =
        Diary.builder().studentId(diary.studentId()).date(diary.date()).note(diary.note()).build();

    diaryRepository.save(newDiary);
    return newDiary;
  }

  public void deleteDiary(Long id) {
    diaryRepository.deleteById(id);
  }

  public Diary updateDiary(Long id, DiaryUpdate diary) {
    Diary updatedDiary =
        diaryRepository.findById(id).orElseThrow(() -> new RuntimeException("Diary not found"));

    updatedDiary.setNote(diary.note());

    diaryRepository.save(updatedDiary);
    return updatedDiary;
  }

  private DiaryView mapDiaryToDiaryView(Diary diary) {
    var date = diary.getDate();
    var studentId = diary.getStudentId();
    var tasks = taskService.getTasksByStudentIdAndDate(studentId, date);
    var deadlines = deadlineService.getDeadlinesByStudentIdAndDate(studentId, date);

    return new DiaryView(
        diary.getId(), diary.getStudentId(), date, diary.getNote(), tasks, deadlines);
  }
}
