package com.orbital.planNUS.diary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/diaries")
public class DiaryController {
  private final DiaryService diaryService;

  public DiaryController(DiaryService diaryService) {
    this.diaryService = diaryService;
  }

  @GetMapping
  public ResponseEntity<List<DiaryViewResponse>> getDiaries(@RequestParam @NotNull @Positive Long studentId) {
    return ResponseEntity.ok(diaryService.getDiariesByStudentId(studentId));
  }

  @GetMapping("{date}")
  public ResponseEntity<DiaryViewResponse> getsertDiary(
      @RequestParam @NotNull @Positive Long studentId, @PathVariable LocalDate date) {
    return ResponseEntity.ok(diaryService.getsertDiaryByStudentId(studentId, date));
  }

  @PostMapping
  public ResponseEntity<Diary> createDiary(@Valid @RequestBody DiaryCreateRequest req) {
    return ResponseEntity.ok(diaryService.addDiary(req));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteDiary(@PathVariable @Positive Long id) {
    diaryService.deleteDiary(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("{id}")
  public ResponseEntity<Diary> updateDiary(
      @PathVariable @Positive Long id, @Valid @RequestBody DiaryUpdateRequest req) {
    return ResponseEntity.ok(diaryService.updateDiary(id, req));
  }
}
