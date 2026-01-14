package com.ipd.energy.service;

import com.ipd.energy.dto.CreateWorkerRequest;
import com.ipd.energy.dto.UserDTO;
import com.ipd.energy.entity.Worker;
import com.ipd.energy.repository.UserRepository;
import com.ipd.energy.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllWorkers() {
        return workerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getWorkerByEmail(String email) {
        Worker worker = workerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Worker not found with email: " + email));
        return convertToDTO(worker);
    }

    public UserDTO createWorker(CreateWorkerRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Create new worker
        Worker worker = new Worker();
        worker.setEmail(request.getEmail());
        worker.setFirstName(request.getFirstName());
        worker.setLastName(request.getLastName());
        worker.setPassword(passwordEncoder.encode(request.getPassword()));
        worker.setPhoneNumber(request.getPhoneNumber());
        worker.setBirthDate(request.getBirthDate());
        worker.setRole("WORKER");

        Worker savedWorker = workerRepository.save(worker);
        return convertToDTO(savedWorker);
    }

    public void deleteWorker(String email) {
        Worker worker = workerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Worker not found with email: " + email));
        workerRepository.delete(worker);
    }

    private UserDTO convertToDTO(Worker worker) {
        return new UserDTO(
                worker.getEmail(),
                worker.getFirstName(),
                worker.getLastName(),
                worker.getPhoneNumber(),
                worker.getRole(),
                worker.getRating());
    }
}
