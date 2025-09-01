package com.darkness.taskManagement.service;

import com.darkness.taskManagement.domain.Task;
import com.darkness.taskManagement.domain.TaskStatusEnum;
import com.darkness.taskManagement.domain.User;
import com.darkness.taskManagement.repository.TaskRepository;
import com.darkness.taskManagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public TaskServiceImpl(final UserRepository userRepository, final TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        Long userId = task.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        task.setUser(user);
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
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }

    @Override
    public List<Task> getTasksByUserAndStatus(Long userId, TaskStatusEnum status) {
        return taskRepository.findByUserIdAndStatus(userId, status);
    }
}