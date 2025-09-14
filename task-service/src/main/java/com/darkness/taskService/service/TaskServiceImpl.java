package com.darkness.taskService.service;

import com.darkness.taskService.domain.Task;
import com.darkness.taskService.domain.TaskStatusEnum;
import com.darkness.taskService.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author darkness
 **/
@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    public TaskServiceImpl(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, Task task) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setStatus(task.getStatus());
        existing.setPriority(task.getPriority());
        existing.setDueDate(task.getDueDate());
        return taskRepository.save(existing);
    }

    @Override
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> getTasksByUser(String userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public List<Task> getTasksByUserAndStatus(String userId, TaskStatusEnum status) {
        return taskRepository.findByUserIdAndStatus(userId, status);
    }
}