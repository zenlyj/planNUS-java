package com.orbital.planNUS.deadline;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/api/deadlines")
public class DeadlineController {
  private final DeadlineService deadlineService;

  public DeadlineController(DeadlineService deadlineService) {
    this.deadlineService = deadlineService;
  }

  @GetMapping
  public ResponseEntity<List<Deadline>> getDeadlines(
      @RequestParam @NotNull @Positive Long studentId) {
    return ResponseEntity.ok(deadlineService.getDeadlinesByStudentId(studentId));
  }

  @PostMapping
  public ResponseEntity<Deadline> createDeadline(@Valid @RequestBody DeadlineCreateRequest req) {
    return ResponseEntity.ok(deadlineService.addDeadline(req));
  }

  @DeleteMapping("{id}")
  public ResponseEntity<Void> deleteDeadline(@PathVariable @Positive Long id) {
    deadlineService.deleteDeadline(id);
    return ResponseEntity.noContent().build();
  }

  @PutMapping("{id}")
  public ResponseEntity<Deadline> updateDeadline(
      @PathVariable @Positive Long id, @Valid @RequestBody DeadlineUpdateRequest req) {
    return ResponseEntity.ok(deadlineService.updateDeadline(id, req));
  }
}
