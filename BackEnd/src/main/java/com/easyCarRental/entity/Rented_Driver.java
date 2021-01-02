package com.easyCarRental.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Rented_Driver {

    @Id
    String id;

    @ManyToOne
    @JoinColumn(name = "reqId", referencedColumnName = "requestId")
    RentalRequest rentalRequest;

    @ManyToOne
    @JoinColumn(name = "driverId", referencedColumnName = "id")
    Driver driver;
}
