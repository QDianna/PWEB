package com.mobylab.springbackend.service;

import com.mobylab.springbackend.entity.TaskList;
import com.mobylab.springbackend.entity.User;
import com.mobylab.springbackend.exception.BadRequestException;
import com.mobylab.springbackend.exception.NotFoundException;
import com.mobylab.springbackend.repository.TaskListRepository;
import com.mobylab.springbackend.repository.UserRepository;
import com.mobylab.springbackend.service.dto.TaskListDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TaskListService extends BasicsService {

    private final TaskListRepository taskListRepository;

    public TaskListService(TaskListRepository taskListRepository,
                           UserRepository userRepository) {
        super(userRepository);
        this.taskListRepository = taskListRepository;
    }

    private TaskListDto mapToDto(TaskList taskList) {
        return new TaskListDto()
                .setId(taskList.getId())
                .setTitle(taskList.getTitle());
    }

    private void checkDtoData(TaskListDto dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new BadRequestException("Tasklist must be named");
        }
    }


    // admin only
    public List<TaskListDto> getTaskListsByUserId(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        List<TaskList> taskLists = taskListRepository.findAllByUserId(userId);
        return taskLists.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // user friendly
    public List<TaskListDto> getTaskListsCurrentUser() {
        User currentUser = getCurrentUser();
        return getTaskListsByUserId(currentUser.getId());
    }

    // user friendly
    public TaskListDto getTaskListById(UUID listId) {
        User currentUser = getCurrentUser();

        TaskList taskList = taskListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("TaskList not found"));

        checkOwnershipOrAdmin(taskList.getUser(), currentUser);
        return mapToDto(taskList);
    }

    // admin only
    public TaskListDto createTaskListForUserId(TaskListDto dto, UUID userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return createTaskListForUser(dto, user);
    }

    // user friendly
    public TaskListDto createTaskListForCurrentUser(TaskListDto dto) {
        User currentUser = getCurrentUser();
        return createTaskListForUser(dto, currentUser);
    }

    // internal
    private TaskListDto createTaskListForUser(TaskListDto dto, User user) {
        checkDtoData(dto);

        TaskList taskList = new TaskList()
                .setTitle(dto.getTitle())
                .setUser(user);

        TaskList saved = taskListRepository.save(taskList);
        return mapToDto(saved);
    }

    // user friendly
    public TaskListDto updateTaskList(UUID listId, TaskListDto dto) {
        checkDtoData(dto);

        User currentUser = getCurrentUser();

        TaskList taskList = taskListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("TaskList not found"));

        checkOwnershipOrAdmin(taskList.getUser(), currentUser);

        taskList.setTitle(dto.getTitle());
        TaskList updated = taskListRepository.save(taskList);
        return mapToDto(updated);
    }

    // user friendly
    public void deleteTaskList(UUID listId) {
        User currentUser = getCurrentUser();

        TaskList taskList = taskListRepository.findById(listId)
                .orElseThrow(() -> new NotFoundException("TaskList not found"));

        checkOwnershipOrAdmin(taskList.getUser(), currentUser);

        taskListRepository.delete(taskList);
    }

}
