package com.darkness.taskManagement.repository;

import com.darkness.taskManagement.domain.Task;
import com.darkness.taskManagement.domain.TaskStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
    List<Task> findByUserIdAndStatus(Long userId, TaskStatusEnum status);
}

