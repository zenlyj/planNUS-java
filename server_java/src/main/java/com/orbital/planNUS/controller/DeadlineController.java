package com.orbital.planNUS.controller;

import com.orbital.planNUS.dto.deadline.DeadlineCreate;
import com.orbital.planNUS.dto.deadline.DeadlineUpdate;
import com.orbital.planNUS.model.Deadline;
import com.orbital.planNUS.service.DeadlineService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deadlines")
public class DeadlineController {
    private final DeadlineService deadlineService;

    public DeadlineController(DeadlineService deadlineService) {
        this.deadlineService = deadlineService;
    }

    @GetMapping
    public List<Deadline> getDeadlines(@RequestParam Long studentId) {
        return deadlineService.getDeadlinesByStudentId(studentId);
    }

    @PostMapping
    public Deadline createDeadline(@RequestBody DeadlineCreate deadline) {
        return deadlineService.addDeadline(deadline);
    }

    @DeleteMapping("{id}")
    public void deleteDeadline(@PathVariable Long id) {
        deadlineService.deleteDeadline(id);
    }

    @PutMapping("{id}")
    public Deadline updateDeadline(@PathVariable Long id, @RequestBody DeadlineUpdate deadline) {
        return deadlineService.updateDeadline(id, deadline);
    }
}
