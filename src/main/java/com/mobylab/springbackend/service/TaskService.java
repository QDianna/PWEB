package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.entity.Task;
import com.mobylab.springbackend.entity.TaskList;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.repository.TaskRepository;
import com.mobylab.springbackend.repository.TaskListRepository;
import com.mobylab.springbackend.service.dto.TaskDto;
import com.mobylab.springbackend.exception.NotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskService extends BasicsService {

    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskService(TaskRepository taskRepository, TaskListRepository taskListRepository, UserRepository userRepository) {
        super(userRepository);
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    private TaskDto mapToTaskDto(Task task) {
        return new TaskDto()
                .setId(task.getId())
                .setContent(task.getContent())
                .setTaskListId(task.getTaskList().getId());
    }

    private Task mapToTaskEntity(TaskDto dto, TaskList taskList) {
        return new Task()
                .setContent(dto.getContent())
                .setTaskList(taskList);
    }

    private void checkDtoData(TaskDto dto) {
        if (dto.getContent() == null) {
            throw new BadRequestException("Task cannot be empty");
        }
    }

    // admin only
    public List<TaskDto> getTasksByUserId(UUID userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));
        List<Task> tasks = taskRepository.findAllByUserId(userId);
        return tasks.stream()
                .map(this::mapToTaskDto)
                .collect(Collectors.toList());
    }

    // user friendly
    public List<TaskDto> getTasksForCurrentUser() {
        User user = getCurrentUser();
        return getTasksByUserId(user.getId());
    }

    // user friendly
    public TaskDto getTaskById(UUID taskId) {
        User currentUser = getCurrentUser();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        checkOwnershipOrAdmin(task.getTaskList().getUser(), currentUser);

        return mapToTaskDto(task);
    }

    // user friendly
    public List<TaskDto> getTasksByTaskListId(UUID taskListId) {
        User currentUser = getCurrentUser();

        TaskList taskList = taskListRepository.findById(taskListId)
                .orElseThrow(() -> new NotFoundException("TaskList not found"));

        checkOwnershipOrAdmin(taskList.getUser(), currentUser);

        return taskList.getTasks().stream()
                .map(this::mapToTaskDto)
                .collect(Collectors.toList());
    }

    // user friendly
    public TaskDto createTask(TaskDto dto) {
        checkDtoData(dto);

        User currentUser = getCurrentUser();

        TaskList taskList = taskListRepository.findById(dto.getTaskListId())
                .orElseThrow(() -> new NotFoundException("TaskList not found"));

        checkOwnershipOrAdmin(taskList.getUser(), currentUser);

        Task task = new Task()
                .setContent(dto.getContent())
                .setTaskList(taskList);

        Task saved = taskRepository.save(task);
        return mapToTaskDto(saved);
    }

    // user friendly
    public TaskDto updateTask(UUID taskId, TaskDto dto) {
        checkDtoData(dto);

        User currentUser = getCurrentUser();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        checkOwnershipOrAdmin(task.getTaskList().getUser(), currentUser);

        task.setContent(dto.getContent());
        Task updated = taskRepository.save(task);
        return mapToTaskDto(updated);
    }

    // user friendly
    public void deleteTask(UUID taskId) {
        User currentUser = getCurrentUser();

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        checkOwnershipOrAdmin(task.getTaskList().getUser(), currentUser);

        taskRepository.delete(task);
    }
}

