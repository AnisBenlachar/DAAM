package com.ipd.energy.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ADMINS")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Admin extends User {

    @Column(name = "permissions", length = 500)
    private String permissions;

    public Admin(String email, String firstName, String lastName, String password,
            LocalDate birthDate, String phoneNumber, String permissions) {
        super(email, firstName, lastName, password, birthDate, phoneNumber, "ADMIN");
        this.permissions = permissions;
    }
}
