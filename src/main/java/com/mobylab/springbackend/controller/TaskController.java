package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.TaskService;
import com.mobylab.springbackend.service.dto.TaskDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController implements SecuredRestController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // GET all tasks for current user
    @GetMapping("/my_tasks")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskDto>> getTasksForCurrentUser() {
        List<TaskDto> tasks = taskService.getTasksForCurrentUser();
        return ResponseEntity.ok(tasks);
    }

    // GET all tasks from a specific task list (admin only or owner)
    @GetMapping("/by-tasklist")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskDto>> getTasksByTaskList(@RequestParam("taskListId") UUID taskListId) {
        List<TaskDto> tasks = taskService.getTasksByTaskListId(taskListId);
        return ResponseEntity.ok(tasks);
    }

    // GET specific task by ID (if owned)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<TaskDto> getTaskById(@PathVariable UUID id) {
        TaskDto task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    // POST create task (must be assigned to a task list)
    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<TaskDto> createTask(@RequestBody TaskDto dto) {
        TaskDto created = taskService.createTask(dto);
        return ResponseEntity.status(201).body(created);
    }

    // PUT update task content
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<TaskDto> updateTask(@PathVariable UUID id, @RequestBody TaskDto dto) {
        TaskDto updated = taskService.updateTask(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE task
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
