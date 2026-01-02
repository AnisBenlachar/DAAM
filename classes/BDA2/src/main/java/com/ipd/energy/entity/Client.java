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
@Table(name = "CLIENTS")
@Data
@EqualsAndHashCode(callSuper = true, exclude = "tasks")
@NoArgsConstructor
@AllArgsConstructor
public class Client extends User {

    @Embedded
    private Location location;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    public Client(String email, String firstName, String lastName, String password, 
                  LocalDate birthDate, String phoneNumber, Location location) {
        super(email, firstName, lastName, password, birthDate, phoneNumber, "CLIENT");
        this.location = location;
    }
}
