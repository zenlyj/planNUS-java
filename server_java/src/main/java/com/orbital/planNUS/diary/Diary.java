package com.orbital.planNUS.diary;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "diaries",
    indexes = {@Index(name = "idx_diaries_student_id", columnList = "studentId")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long studentId;

  @Column(nullable = false)
  private LocalDate date;

  private String note;
}
