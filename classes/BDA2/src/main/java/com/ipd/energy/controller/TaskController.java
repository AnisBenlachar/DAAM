package com.ipd.energy.controller;

import com.ipd.energy.dto.TaskDTO;
import com.ipd.energy.dto.UpdateTaskStatusRequest;
import com.ipd.energy.entity.Task;
import com.ipd.energy.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    public ResponseEntity<List<TaskDTO>> getAllPendingTasks() {
        try {
            List<TaskDTO> tasks = taskService.getAllPendingTasks();
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/worker/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    public ResponseEntity<List<TaskDTO>> getTasksByWorker(@PathVariable String email, Authentication authentication) {
        // Workers can only see their own tasks unless they're admin
        String currentUserEmail = (String) authentication.getPrincipal();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !currentUserEmail.equals(email)) {
            return ResponseEntity.status(403).build();
        }

        try {
            List<TaskDTO> tasks = taskService.getTasksByWorker(email);
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            System.err.println("REST ERROR in getTasksByWorker: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/client/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<TaskDTO>> getTasksByClient(@PathVariable String email) {
        List<TaskDTO> tasks = taskService.getTasksByClient(email);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER', 'CLIENT')")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long id) {
        try {
            TaskDTO task = taskService.getTaskById(id);
            return ResponseEntity.ok(task);
        } catch (RuntimeException e) {
            System.err.println("REST ERROR in getTaskById: " + e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            System.err.println("REST UNEXPECTED ERROR in getTaskById: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskStatusRequest request,
            Authentication authentication) {
        try {
            String workerEmail = null;
            // If it's a worker, we might want to automatically assign them if they are
            // accepting it
            // but the DTO doesn't have workerEmail yet. Let's see if we should add it to
            // UpdateTaskStatusRequest
            // or use authentication principal.

            // For now, let's assume if status is CONFIRMED and it's a WORKER, they are
            // accepting it.
            if (request.getStatus() == Task.TaskStatus.CONFIRMED) {
                workerEmail = (String) authentication.getPrincipal();
            }

            TaskDTO updatedTask = taskService.updateTaskStatus(id, request.getStatus(), workerEmail);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) {
        try {
            TaskDTO createdTask = taskService.createTask(taskDTO);
            return ResponseEntity.ok(createdTask);
        } catch (Exception e) {
            System.err.println("REST ERROR in createTask: " + e.getMessage());
            return ResponseEntity.status(500).build();
        }
    }
}
