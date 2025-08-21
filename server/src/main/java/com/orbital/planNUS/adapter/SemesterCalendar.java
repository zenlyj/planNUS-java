package com.orbital.planNUS.adapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SemesterCalendar {
  public static List<List<LocalDate>> weeklyDates(int numWeeks, LocalDate startDate) {
    List<List<LocalDate>> weeks = new ArrayList<>();
    var date = startDate;
    for (int i = 1; i <= numWeeks; i++) {
      List<LocalDate> week = new ArrayList<>();
      if (i == 7) {
        // recess week
        date = date.plusDays(7);
      }
      for (int j = 0; j < 7; j++) {
        week.add(date);
        date = date.plusDays(1);
      }
      weeks.add(Collections.unmodifiableList(week));
    }
    return Collections.unmodifiableList(weeks);
  }
}
