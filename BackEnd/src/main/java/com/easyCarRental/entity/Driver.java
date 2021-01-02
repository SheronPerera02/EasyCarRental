package com.easyCarRental.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Driver {
    @Id
    private String id;
    private String name;
    private String contact;
    private String isAvailable;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    List<Rented_Driver> rentedDrivers = new ArrayList<Rented_Driver>();

}
