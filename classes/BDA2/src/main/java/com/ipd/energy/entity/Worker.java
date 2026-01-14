package com.ipd.energy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "WORKERS")
@Data
@EqualsAndHashCode(callSuper = true, exclude = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Worker extends User {

    @OneToMany(mappedBy = "worker", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    @Column(name = "rating")
    private Double rating = 0.0;

    public Worker(String email, String firstName, String lastName, String password,
            LocalDate birthDate, String phoneNumber) {
        super(email, firstName, lastName, password, birthDate, phoneNumber, "WORKER");
    }
}
