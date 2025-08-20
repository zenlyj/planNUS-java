package com.orbital.planNUS.task;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long studentId;
  private String name;
  private String module;
  private String description;
  private LocalTime startTime;
  private LocalTime endTime;
  private LocalDate date;
  private Boolean isCompleted;
}
