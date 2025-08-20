package com.orbital.planNUS.deadline;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(
    name = "deadlines",
    indexes = {@Index(name = "idx_deadlines_student_id", columnList = "studentId")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deadline {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long studentId;

  @Column(nullable = false)
  private LocalDate date;

  private String name;
  private String module;
  private String description;
}
