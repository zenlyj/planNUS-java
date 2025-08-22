package com.orbital.planNUS.diary;

import com.orbital.planNUS.deadline.DeadlineService;
import com.orbital.planNUS.exception.AppException;
import com.orbital.planNUS.task.TaskService;
import jakarta.transaction.Transactional;
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

  @Transactional
  public List<DiaryViewResponse> getDiariesByStudentId(Long studentId) {
    var diaries = diaryRepository.findByStudentId(studentId);
    return diaries.stream().map(this::mapDiaryToDiaryView).toList();
  }

  @Transactional
  public DiaryViewResponse getsertDiaryByStudentId(Long studentId, LocalDate date) {
    Optional<Diary> diary =
        diaryRepository.findByStudentIdAndDate(studentId, date).stream().findFirst();

    if (diary.isPresent()) {
      return mapDiaryToDiaryView(diary.get());
    }

    var newDiary = Diary.builder().studentId(studentId).date(date).note("").build();
    diaryRepository.save(newDiary);
    return mapDiaryToDiaryView(newDiary);
  }

  public Diary addDiary(DiaryCreateRequest req) {
    var newDiary =
        Diary.builder().studentId(req.studentId()).date(req.date()).note(req.note()).build();

    diaryRepository.save(newDiary);
    return newDiary;
  }

  public void deleteDiary(Long id) {
    diaryRepository.deleteById(id);
  }

  public Diary updateDiary(Long id, DiaryUpdateRequest req) {
    var updatedDiary =
        diaryRepository.findById(id).orElseThrow(() -> new AppException("id", "Diary not found"));

    updatedDiary.setNote(req.note());

    diaryRepository.save(updatedDiary);
    return updatedDiary;
  }

  private DiaryViewResponse mapDiaryToDiaryView(Diary diary) {
    var date = diary.getDate();
    var studentId = diary.getStudentId();
    var tasks = taskService.getTasksByStudentIdAndDate(studentId, date);
    var deadlines = deadlineService.getDeadlinesByStudentIdAndDate(studentId, date);

    return new DiaryViewResponse(
        diary.getId(), diary.getStudentId(), date, diary.getNote(), tasks, deadlines);
  }
}
