package com.orbital.planNUS.deadline;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deadlines")
public class DeadlineController {
    private final DeadlineService deadlineService;

    public DeadlineController(DeadlineService deadlineService) {
        this.deadlineService = deadlineService;
    }

    @GetMapping
    public ResponseEntity<List<Deadline>> getDeadlines(@RequestParam Long studentId) {
        return ResponseEntity.ok(deadlineService.getDeadlinesByStudentId(studentId));
    }

    @PostMapping
    public ResponseEntity<Deadline> createDeadline(@RequestBody DeadlineCreateRequest req) {
        return ResponseEntity.ok(deadlineService.addDeadline(req));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteDeadline(@PathVariable Long id) {
        deadlineService.deleteDeadline(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<Deadline> updateDeadline(@PathVariable Long id, @RequestBody DeadlineUpdateRequest req) {
        return ResponseEntity.ok(deadlineService.updateDeadline(id, req));
    }
}
