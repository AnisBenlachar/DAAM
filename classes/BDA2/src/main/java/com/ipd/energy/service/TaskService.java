package com.ipd.energy.service;

import com.ipd.energy.dto.TaskDTO;
import com.ipd.energy.entity.Product;
import com.ipd.energy.entity.Task;
import com.ipd.energy.entity.Task.TaskStatus;
import com.ipd.energy.entity.Client;
import com.ipd.energy.entity.Worker;
import com.ipd.energy.repository.ClientRepository;
import com.ipd.energy.repository.ProductRepository;
import com.ipd.energy.repository.TaskRepository;
import com.ipd.energy.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<TaskDTO> getAllTasks() {
        try {
            return taskRepository.findAllOrderByCreatedAtDesc().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("ERROR in getAllTasks: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByWorker(String workerEmail) {
        try {
            return taskRepository.findByWorkerEmail(workerEmail).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("ERROR in getTasksByWorker for " + workerEmail + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getTasksByClient(String clientEmail) {
        try {
            return taskRepository.findByClientEmail(clientEmail).stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("ERROR in getTasksByClient for " + clientEmail + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<TaskDTO> getAllPendingTasks() {
        try {
            return taskRepository.findAllPendingTasks().stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("ERROR in getAllPendingTasks: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public TaskDTO getTaskById(Long id) {
        try {
            Task task = taskRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
            return convertToDTO(task);
        } catch (Exception e) {
            System.err.println("ERROR in getTaskById for id " + id + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Transactional
    public TaskDTO updateTaskStatus(Long id, TaskStatus newStatus, String workerEmail) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));

        task.setStatus(newStatus);

        // If a worker email is provided (common when accepting a request), assign the
        // worker
        if (workerEmail != null && !workerEmail.isEmpty()) {
            Worker worker = workerRepository.findByEmail(workerEmail)
                    .orElseThrow(() -> new RuntimeException("Worker not found: " + workerEmail));
            task.setWorker(worker);
        }

        // Update timestamps based on status
        if (newStatus == TaskStatus.CONFIRMED && task.getConfirmedAt() == null) {
            task.setConfirmedAt(LocalDateTime.now());
        } else if (newStatus == TaskStatus.COMPLETED && task.getAchievedAt() == null) {
            task.setAchievedAt(LocalDateTime.now());
        }

        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    @Transactional
    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus() != null ? taskDTO.getStatus() : Task.TaskStatus.PENDING);
        task.setCreatedAt(LocalDateTime.now());

        if (taskDTO.getClientEmail() != null) {
            Client client = clientRepository.findByEmail(taskDTO.getClientEmail())
                    .orElseThrow(() -> new RuntimeException("Client not found: " + taskDTO.getClientEmail()));
            task.setClient(client);
        }

        if (taskDTO.getWorkerEmail() != null) {
            Worker worker = workerRepository.findByEmail(taskDTO.getWorkerEmail())
                    .orElseThrow(() -> new RuntimeException("Worker not found: " + taskDTO.getWorkerEmail()));
            task.setWorker(worker);
        }

        if (taskDTO.getProductId() != null) {
            Product product = productRepository.findById(taskDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + taskDTO.getProductId()));
            task.setProduct(product);
        }

        Task savedTask = taskRepository.save(task);
        return convertToDTO(savedTask);
    }

    private TaskDTO convertToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setCreatedAt(task.getCreatedAt());
        dto.setConfirmedAt(task.getConfirmedAt());
        dto.setAchievedAt(task.getAchievedAt());

        if (task.getClient() != null) {
            dto.setClientEmail(task.getClient().getEmail());
            dto.setClientName(task.getClient().getFirstName() + " " + task.getClient().getLastName());
        }

        if (task.getWorker() != null) {
            dto.setWorkerEmail(task.getWorker().getEmail());
            dto.setWorkerName(task.getWorker().getFirstName() + " " + task.getWorker().getLastName());
        }

        if (task.getProduct() != null) {
            dto.setProductId(task.getProduct().getId());
            dto.setProductName(task.getProduct().getName());
        }

        return dto;
    }
}
