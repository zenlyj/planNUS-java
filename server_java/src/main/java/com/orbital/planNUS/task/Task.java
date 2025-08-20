package com.orbital.planNUS.task;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
    name = "tasks",
    indexes = {@Index(name = "idx_tasks_student_id", columnList = "studentId")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long studentId;

  @Column(nullable = false)
  private LocalTime startTime;

  @Column(nullable = false)
  private LocalTime endTime;

  @Column(nullable = false)
  private LocalDate date;

  @Column(nullable = false)
  private Boolean isCompleted;

  private String name;
  private String module;
  private String description;
}
