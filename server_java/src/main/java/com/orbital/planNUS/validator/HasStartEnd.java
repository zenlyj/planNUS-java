package com.orbital.planNUS.validator;

import java.time.LocalTime;

public interface HasStartEnd {
  LocalTime startTime();

  LocalTime endTime();
}
