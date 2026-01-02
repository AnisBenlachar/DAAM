package com.ipd.energy.controller;

import com.ipd.energy.dto.CreateWorkerRequest;
import com.ipd.energy.dto.UserDTO;
import com.ipd.energy.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/workers")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:3001" })
public class WorkerController {

    @Autowired
    private WorkerService workerService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    public ResponseEntity<List<UserDTO>> getAllWorkers() {
        List<UserDTO> workers = workerService.getAllWorkers();
        return ResponseEntity.ok(workers);
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'WORKER')")
    public ResponseEntity<UserDTO> getWorkerByEmail(@PathVariable String email) {
        try {
            UserDTO worker = workerService.getWorkerByEmail(email);
            return ResponseEntity.ok(worker);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createWorker(@Valid @RequestBody CreateWorkerRequest request) {
        try {
            UserDTO worker = workerService.createWorker(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(worker);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteWorker(@PathVariable String email) {
        try {
            workerService.deleteWorker(email);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
