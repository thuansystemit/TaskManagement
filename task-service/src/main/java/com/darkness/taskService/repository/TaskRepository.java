package com.darkness.taskService.repository;

import com.darkness.taskService.domain.Task;
import com.darkness.taskService.domain.TaskStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @author darkness
 **/
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(String userId);
    List<Task> findByUserIdAndStatus(String userId, TaskStatusEnum status);
}

