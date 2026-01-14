package com.ipd.energy.repository;

import com.ipd.energy.entity.Task;
import com.ipd.energy.entity.Task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t WHERE t.worker.email = :workerEmail")
    List<Task> findByWorkerEmail(@Param("workerEmail") String workerEmail);

    @Query("SELECT t FROM Task t WHERE t.client.email = :clientEmail")
    List<Task> findByClientEmail(@Param("clientEmail") String clientEmail);

    List<Task> findByStatus(TaskStatus status);

    @Query("SELECT t FROM Task t WHERE t.status = 'PENDING'")
    List<Task> findAllPendingTasks();

    @Query("SELECT t FROM Task t ORDER BY t.createdAt DESC")
    List<Task> findAllOrderByCreatedAtDesc();
}
