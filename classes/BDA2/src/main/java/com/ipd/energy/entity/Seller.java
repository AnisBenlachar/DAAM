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
@Table(name = "SELLERS")
@Data
@EqualsAndHashCode(callSuper = true, exclude = "productItems")
@NoArgsConstructor
@AllArgsConstructor
public class Seller extends User {

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductItem> productItems = new ArrayList<>();

    public Seller(String email, String firstName, String lastName, String password,
            LocalDate birthDate, String phoneNumber) {
        super(email, firstName, lastName, password, birthDate, phoneNumber, "SELLER");
    }
}
