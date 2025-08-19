package com.orbital.planNUS.dto.diary;

import java.time.LocalDate;

public record DiaryCreate(Long studentId, LocalDate date, String note) {}
