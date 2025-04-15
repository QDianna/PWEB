package com.mobylab.springbackend.controller;

import com.mobylab.springbackend.service.TaskListService;
import com.mobylab.springbackend.service.dto.TaskListDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasklists")
public class TaskListController implements SecuredRestController {

    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    // GET all task lists for current user
    @GetMapping("/my_lists")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskListDto>> getTaskListsForCurrentUser() {
        List<TaskListDto> lists = taskListService.getTaskListsCurrentUser();
        return ResponseEntity.ok(lists);
    }

    // GET specific task list by ID (if owned)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<TaskListDto> getTaskListById(@PathVariable UUID id) {
        TaskListDto dto = taskListService.getTaskListById(id);
        return ResponseEntity.ok(dto);
    }

    // POST create task list for current user
    @PostMapping
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<TaskListDto> createTaskListForCurrentUser(@RequestBody TaskListDto dto) {
        TaskListDto created = taskListService.createTaskListForCurrentUser(dto);
        return ResponseEntity.status(201).body(created);
    }

    // PUT update task list title (if owned)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<TaskListDto> updateTaskList(@PathVariable UUID id, @RequestBody TaskListDto dto) {
        TaskListDto updated = taskListService.updateTaskList(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE task list (if owned)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteTaskList(@PathVariable UUID id) {
        taskListService.deleteTaskList(id);
        return ResponseEntity.noContent().build();
    }

    ////////////////////////// ADMIN ONLY //////////////////////////

    // GET task lists by user ID (admin only)
    @GetMapping("/by-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskListDto>> getTaskListsByUserId(@RequestParam UUID userId) {
        List<TaskListDto> lists = taskListService.getTaskListsByUserId(userId);
        return ResponseEntity.ok(lists);
    }

    // POST create task list for a user (admin only)
    @PostMapping("/by-user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<TaskListDto> createTaskListForUser(@RequestParam UUID userId,
                                                             @RequestBody TaskListDto dto) {
        TaskListDto created = taskListService.createTaskListForUserId(dto, userId);
        return ResponseEntity.status(201).body(created);
    }
}
