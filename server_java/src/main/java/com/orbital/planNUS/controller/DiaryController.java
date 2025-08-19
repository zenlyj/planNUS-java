package com.orbital.planNUS.controller;

import com.orbital.planNUS.dto.diary.DiaryCreate;
import com.orbital.planNUS.dto.diary.DiaryUpdate;
import com.orbital.planNUS.dto.diary.DiaryView;
import com.orbital.planNUS.model.Diary;
import com.orbital.planNUS.service.DiaryService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/diaries")
public class DiaryController {
    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @GetMapping
    public List<DiaryView> getDiaries(@RequestParam Long studentId) {
        return diaryService.getDiariesByStudentId(studentId);
    }

    @GetMapping("{date}")
    public DiaryView getsertDiary(@RequestParam Long studentId, @PathVariable LocalDate date) {
        return diaryService.getsertDiaryByStudentId(studentId, date);
    }

    @PostMapping
    public Diary createDiary(@RequestBody DiaryCreate diary) {
        return diaryService.addDiary(diary);
    }

    @DeleteMapping("{id}")
    public void deleteDiary(@PathVariable Long id) {
        diaryService.deleteDiary(id);
    }

    @PatchMapping("{id}")
    public Diary updateDiary(@PathVariable Long id, @RequestBody DiaryUpdate diary) {
        return diaryService.updateDiary(id, diary);
    }
}
