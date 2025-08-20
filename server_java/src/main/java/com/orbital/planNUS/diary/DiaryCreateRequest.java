package com.orbital.planNUS.diary;

import java.time.LocalDate;

public record DiaryCreateRequest(Long studentId, LocalDate date, String note) {}
