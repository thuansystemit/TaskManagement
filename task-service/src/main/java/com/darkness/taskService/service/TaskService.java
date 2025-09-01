package com.darkness.taskService.service;

import com.darkness.taskService.domain.Task;
import com.darkness.taskService.domain.TaskStatusEnum;

import java.util.List;

public interface TaskService {
    Task createTask(Task task);
    Task updateTask(Long id, Task task);
    void deleteTask(Long id);
    List<Task> getTasksByUser(Long userId);
    List<Task> getTasksByUserAndStatus(Long userId, TaskStatusEnum status);
}
